package com.example.nl2mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public record MongoQueryPlan(
        String collection,
        String operation,
        String filterJson,
        String projectionJson,
        String sortJson,
        int limit,
        List<String> pipelineJsonStages
) {
    public static MongoQueryPlan fromJson(String json) {
        JSONObject root = new JSONObject(json);

        String collection = root.optString("collection", "");
        String operation = root.optString("operation", "find");
        if (operation == null || operation.isBlank()) {
            operation = "find";
        }

        String normalizedOperation = operation.trim().toLowerCase(Locale.ROOT);
        if (!normalizedOperation.equals("find")
                && !normalizedOperation.equals("countdocuments")
                && !normalizedOperation.equals("count")
                && !normalizedOperation.equals("aggregate")) {
            throw new IllegalArgumentException("Unsupported operation: " + operation);
        }

        String filterJson = "{}";
        if (root.has("filter") && !root.isNull("filter")) {
            Object value = root.get("filter");
            if (value instanceof JSONObject obj) {
                filterJson = obj.toString();
            } else {
                throw new IllegalArgumentException("'filter' must be a JSON object.");
            }
        }

        String projectionJson = null;
        if (root.has("projection") && !root.isNull("projection")) {
            Object value = root.get("projection");
            if (value instanceof JSONObject obj) {
                projectionJson = obj.toString();
            } else {
                throw new IllegalArgumentException("'projection' must be a JSON object.");
            }
        }

        String sortJson = null;
        if (root.has("sort") && !root.isNull("sort")) {
            Object value = root.get("sort");
            if (value instanceof JSONObject obj) {
                sortJson = obj.toString();
            } else {
                throw new IllegalArgumentException("'sort' must be a JSON object.");
            }
        }

        int limit = root.optInt("limit", 0);
        if (limit < 0) {
            throw new IllegalArgumentException("'limit' cannot be negative.");
        }

        List<String> pipelineJsonStages = new ArrayList<>();
        if (root.has("pipeline") && !root.isNull("pipeline")) {
            Object value = root.get("pipeline");
            if (!(value instanceof JSONArray pipeline)) {
                throw new IllegalArgumentException("'pipeline' must be a JSON array.");
            }
            for (int i = 0; i < pipeline.length(); i++) {
                Object stage = pipeline.get(i);
                if (!(stage instanceof JSONObject stageObj)) {
                    throw new IllegalArgumentException("Each pipeline stage must be a JSON object.");
                }
                pipelineJsonStages.add(stageObj.toString());
            }
        }

        return new MongoQueryPlan(
                collection == null ? "" : collection.trim(),
                normalizedOperation,
                filterJson,
                projectionJson,
                sortJson,
                limit,
                pipelineJsonStages
        );
    }

    public Document filterDocument() {
        return Document.parse(filterJson == null ? "{}" : filterJson);
    }

    public Optional<Document> projectionDocument() {
        if (projectionJson == null || projectionJson.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Document.parse(projectionJson));
    }

    public Optional<Document> sortDocument() {
        if (sortJson == null || sortJson.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Document.parse(sortJson));
    }

    public List<Document> pipelineDocuments() {
        List<Document> docs = new ArrayList<>();
        for (String stageJson : pipelineJsonStages) {
            docs.add(Document.parse(stageJson));
        }
        return docs;
    }
}
