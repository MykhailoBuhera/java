package com.example.nl2mongo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class OpenAiMongoTranslator {
    private static final String OPENAI_RESPONSES_URL = "https://api.openai.com/v1/responses";

    private final HttpClient httpClient;
    private final String apiKey;
    private final String model;
    private final String customSystemPrompt;

    public OpenAiMongoTranslator(String apiKey, String model) {
        this(apiKey, model, "");
    }

    public OpenAiMongoTranslator(String apiKey, String model, String customSystemPrompt) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        this.apiKey = apiKey;
        this.model = model;
        this.customSystemPrompt = customSystemPrompt == null ? "" : customSystemPrompt.trim();
    }

    public MongoTranslationResult translate(
            String userRequest,
            String databaseName,
            java.util.List<String> availableCollections,
            String schemaDescription
    ) throws IOException, InterruptedException {
        String systemPrompt = buildSystemPrompt(databaseName, availableCollections, schemaDescription);

        String userMessage = """
                User request:
                %s
                """.formatted(userRequest);

        JSONObject requestBody = new JSONObject()
                .put("model", model)
                .put("temperature", 0)
                .put("input", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "system")
                                .put("content", systemPrompt))
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("content", userMessage)));

        HttpRequest request = HttpRequest.newBuilder(URI.create(OPENAI_RESPONSES_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IllegalStateException("OpenAI API failed with status " + response.statusCode() + ": " + response.body());
        }

        JSONObject responseJson = new JSONObject(response.body());
        String modelText = extractOutputText(responseJson);
        String jsonPlan = extractJsonObject(modelText);
        MongoQueryPlan queryPlan = MongoQueryPlan.fromJson(jsonPlan);
        return new MongoTranslationResult(jsonPlan, queryPlan);
    }

    private String buildSystemPrompt(String databaseName, java.util.List<String> availableCollections, String schemaDescription) {
        String basePrompt = """
                You convert a natural language request into a strict JSON plan for MongoDB.
                Return only JSON without markdown and without explanations.
                Supported operations are read-only:
                - find
                - countDocuments
                - aggregate

                JSON schema:
                {
                  "collection": "string or empty",
                  "operation": "find | countDocuments | aggregate",
                  "filter": { ... },                 // optional, default {}
                  "projection": { ... },             // optional, for find
                  "sort": { ... },                   // optional, for find
                  "limit": 0,                        // optional, integer >= 0, for find
                  "pipeline": [ { ... } ]            // required for aggregate
                }

                Rules:
                1) Never use update/insert/delete/drop commands.
                2) If available collections list is non-empty, choose one exact name from that list.
                   If list is empty, infer collection from database structure context and set collection explicitly.
                3) Use MongoDB Extended JSON when needed (e.g. {"$oid":"..."}).
                4) Keep output machine-readable JSON only.
                """;

        StringBuilder builder = new StringBuilder(basePrompt)
                .append("\n\nTarget database:\n")
                .append(databaseName == null ? "" : databaseName.trim())
                .append("\n\nAvailable collections (choose exactly one of these names):\n")
                .append(formatCollections(availableCollections));

        if (schemaDescription != null && !schemaDescription.isBlank()) {
            builder.append("\n\nDatabase structure context from user:\n")
                    .append(schemaDescription.trim());
        }

        if (customSystemPrompt != null && !customSystemPrompt.isBlank()) {
            builder.append("\n\nAdditional system instructions from user:\n")
                    .append(customSystemPrompt);
        }

        return builder.toString();
    }

    private static String formatCollections(java.util.List<String> availableCollections) {
        if (availableCollections == null || availableCollections.isEmpty()) {
            return "[]";
        }
        JSONArray array = new JSONArray(availableCollections);
        return array.toString();
    }

    private static String extractOutputText(JSONObject responseJson) {
        String outputText = responseJson.optString("output_text");
        if (outputText != null && !outputText.isBlank()) {
            return outputText.trim();
        }

        StringBuilder fallback = new StringBuilder();
        JSONArray output = responseJson.optJSONArray("output");
        if (output == null) {
            throw new IllegalStateException("OpenAI response does not contain output_text or output.");
        }

        for (int i = 0; i < output.length(); i++) {
            JSONObject item = output.optJSONObject(i);
            if (item == null) {
                continue;
            }
            JSONArray content = item.optJSONArray("content");
            if (content == null) {
                continue;
            }
            for (int j = 0; j < content.length(); j++) {
                JSONObject chunk = content.optJSONObject(j);
                if (chunk == null) {
                    continue;
                }
                String text = chunk.optString("text");
                if (text != null && !text.isBlank()) {
                    fallback.append(text).append('\n');
                }
            }
        }

        String value = fallback.toString().trim();
        if (value.isBlank()) {
            throw new IllegalStateException("Could not extract model text from OpenAI response.");
        }
        return value;
    }

    private static String extractJsonObject(String raw) {
        String candidate = stripCodeFences(raw).trim();
        int first = candidate.indexOf('{');
        if (first < 0) {
            throw new IllegalStateException("Model response does not contain JSON object.");
        }

        int depth = 0;
        boolean inString = false;
        boolean escaping = false;
        for (int i = first; i < candidate.length(); i++) {
            char ch = candidate.charAt(i);

            if (inString) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }

            if (ch == '"') {
                inString = true;
                continue;
            }

            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return candidate.substring(first, i + 1);
                }
            }
        }

        throw new IllegalStateException("JSON object appears truncated in model response.");
    }

    private static String stripCodeFences(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("```")) {
            int firstNewLine = trimmed.indexOf('\n');
            if (firstNewLine >= 0) {
                trimmed = trimmed.substring(firstNewLine + 1);
            }
            int closingFence = trimmed.lastIndexOf("```");
            if (closingFence >= 0) {
                trimmed = trimmed.substring(0, closingFence);
            }
        }
        return trimmed.trim();
    }
}
