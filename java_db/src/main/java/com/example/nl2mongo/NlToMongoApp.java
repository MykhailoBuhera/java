package com.example.nl2mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NlToMongoApp {
    private static final Pattern COLLECTION_LINE_PATTERN =
            Pattern.compile("^\\s*#?\\s*Collection\\s*:\\s*([A-Za-z0-9._-]+)\\s*$", Pattern.CASE_INSENSITIVE);

    private NlToMongoApp() {
    }

    public static void main(String[] args) throws Exception {
        Properties config = loadConfig();
        String naturalLanguageRequest = normalizeNaturalLanguageRequest(readNaturalLanguageRequest(args));
        String openAiApiKey = requiredValue(config, "openai.apiKey", "OPENAI_API_KEY");
        String openAiModel = optionalValue(config, "openai.model", "OPENAI_MODEL", "gpt-4.1-mini");
        String systemPromptFile = optionalValue(config, "openai.systemPromptFile", "OPENAI_SYSTEM_PROMPT_FILE",
                "system_prompt.txt");
        String schemaFile = optionalValue(config, "openai.schemaFile", "OPENAI_SCHEMA_FILE",
                "schema_description.txt");
        String mongoUri = requiredValue(config, "mongodb.uri", "MONGODB_URI");
        String mongoDbName = requiredValue(config, "mongodb.db", "MONGODB_DB");
        String customSystemPrompt = loadOptionalTextFile(systemPromptFile);
        String schemaDescription = loadOptionalTextFile(schemaFile);

        try (MongoClient client = MongoClients.create(mongoUri)) {
            MongoDatabase database = client.getDatabase(mongoDbName);
            List<String> mongoCollections = listCollectionNames(database);
            List<String> schemaCollections = extractCollectionsFromSchema(schemaDescription);
            List<String> promptCollections = mergeCollectionNames(mongoCollections, schemaCollections);

            OpenAiMongoTranslator translator = new OpenAiMongoTranslator(openAiApiKey, openAiModel, customSystemPrompt);
            MongoTranslationResult translationResult = translator.translate(
                    naturalLanguageRequest, mongoDbName, promptCollections, schemaDescription);
            printChatGptQuery(translationResult.rawJson());

            MongoQueryPlan finalPlan = applyCollectionHeuristics(
                    naturalLanguageRequest, translationResult.queryPlan(), promptCollections);
            printCollectionCorrectionNotice(translationResult.queryPlan(), finalPlan);
            printExecutedQueryPlan(finalPlan);
            runQuery(database, promptCollections, finalPlan);
        }
    }

    private static String readNaturalLanguageRequest(String[] args) throws IOException {
        if (args != null && args.length > 0) {
            return String.join(" ", args).trim();
        }

        System.out.print("Enter request in plain language: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Request cannot be empty.");
        }
        return line.trim();
    }

    private static Properties loadConfig() throws IOException {
        Properties properties = new Properties();

        String explicitConfigPath = System.getenv("APP_CONFIG_FILE");
        if (explicitConfigPath != null && !explicitConfigPath.isBlank()) {
            Path path = Paths.get(explicitConfigPath.trim());
            loadPropertiesFromPath(path, properties);
            return properties;
        }

        Path localPath = Paths.get("config.properties");
        if (Files.exists(localPath)) {
            loadPropertiesFromPath(localPath, properties);
            return properties;
        }

        try (InputStream stream = NlToMongoApp.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (stream != null) {
                properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
            }
        }
        return properties;
    }

    private static void loadPropertiesFromPath(Path path, Properties properties) throws IOException {
        if (path == null || properties == null || !Files.exists(path)) {
            return;
        }
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
    }

    private static String requiredValue(Properties config, String propertyKey, String envKey) {
        String value = optionalValue(config, propertyKey, envKey, null);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing config. Set '" + propertyKey + "' in config.properties or env '" + envKey + "'.");
        }
        return value;
    }

    private static String optionalValue(Properties config, String propertyKey, String envKey, String defaultValue) {
        String fromFile = config.getProperty(propertyKey);
        if (fromFile != null && !fromFile.isBlank()) {
            return fromFile.trim();
        }
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }
        if (defaultValue == null) {
            return null;
        }
        return defaultValue;
    }

    private static String loadOptionalTextFile(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            return "";
        }
        String trimmed = filePath.trim();
        Path path = Paths.get(trimmed);
        if (!Files.exists(path)) {
            String resourceName = trimmed.replace('\\', '/');
            try (InputStream stream = NlToMongoApp.class.getClassLoader().getResourceAsStream(resourceName)) {
                if (stream == null) {
                    return "";
                }
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
        }
        return Files.readString(path, StandardCharsets.UTF_8).trim();
    }

    private static List<String> listCollectionNames(MongoDatabase database) {
        return database.listCollectionNames().into(new ArrayList<>());
    }

    private static List<String> extractCollectionsFromSchema(String schemaDescription) {
        if (schemaDescription == null || schemaDescription.isBlank()) {
            return List.of();
        }

        Set<String> names = new LinkedHashSet<>();
        String[] lines = schemaDescription.split("\\R");
        for (String line : lines) {
            Matcher matcher = COLLECTION_LINE_PATTERN.matcher(line);
            if (matcher.matches()) {
                names.add(matcher.group(1));
            }
        }
        return new ArrayList<>(names);
    }

    private static List<String> mergeCollectionNames(List<String> mongoCollections, List<String> schemaCollections) {
        Set<String> names = new LinkedHashSet<>();

        if (mongoCollections != null) {
            for (String name : mongoCollections) {
                if (name != null && !name.isBlank()) {
                    names.add(name.trim());
                }
            }
        }

        if (schemaCollections != null) {
            for (String name : schemaCollections) {
                if (name != null && !name.isBlank()) {
                    names.add(name.trim());
                }
            }
        }

        return new ArrayList<>(names);
    }

    private static void runQuery(MongoDatabase database, List<String> availableCollections, MongoQueryPlan plan) {
        String collectionName = resolveCollectionName(plan.collection(), availableCollections);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        String operation = plan.operation().toLowerCase(Locale.ROOT);
        switch (operation) {
            case "find" -> executeFind(collection, plan);
            case "countdocuments", "count" -> executeCount(collection, plan);
            case "aggregate" -> executeAggregate(collection, plan);
            default -> throw new IllegalArgumentException(
                    "Unsupported operation from model: " + operation + ". Allowed: find, countDocuments, aggregate.");
        }
    }

    private static String resolveCollectionName(String modelCollection, List<String> availableCollections) {
        if (availableCollections == null || availableCollections.isEmpty()) {
            if (modelCollection == null || modelCollection.isBlank()) {
                throw new IllegalStateException(
                        "No visible collections in selected DB and model did not choose collection. " +
                                "Check mongodb.db and add collection names to schema_description.txt.");
            }
            return modelCollection.trim();
        }

        if (modelCollection == null || modelCollection.isBlank()) {
            if (availableCollections.size() == 1) {
                return availableCollections.get(0);
            }
            throw new IllegalArgumentException(
                    "Model did not choose a collection. Available: " + formatCollections(availableCollections));
        }

        String value = modelCollection.trim();
        if (availableCollections.contains(value)) {
            return value;
        }

        for (String existing : availableCollections) {
            if (existing.equalsIgnoreCase(value)) {
                return existing;
            }
        }

        System.out.println(
                "Collection is not in discovered list, using as-is: " + value
                        + ". Discovered: " + formatCollections(availableCollections));
        return value;
    }

    private static String formatCollections(List<String> collections) {
        return collections.stream().sorted().collect(Collectors.joining(", "));
    }

    private static void printChatGptQuery(String rawJson) {
        System.out.println("Mongo query plan from ChatGPT:");
        if (rawJson == null || rawJson.isBlank()) {
            System.out.println("{}");
            return;
        }
        try {
            System.out.println(new JSONObject(rawJson).toString(2));
        } catch (Exception ignored) {
            System.out.println(rawJson);
        }
    }

    private static MongoQueryPlan applyCollectionHeuristics(
            String userRequest,
            MongoQueryPlan plan,
            List<String> availableCollections
    ) {
        if (plan == null || userRequest == null || userRequest.isBlank()) {
            return plan;
        }

        String lower = normalizeNaturalLanguageRequest(userRequest).toLowerCase(Locale.ROOT);
        String preferredCollection = detectPreferredCollection(lower, availableCollections);
        if (preferredCollection == null) {
            return plan;
        }

        if (preferredCollection.equalsIgnoreCase(plan.collection())) {
            return plan;
        }

        return new MongoQueryPlan(
                preferredCollection,
                plan.operation(),
                plan.filterJson(),
                plan.projectionJson(),
                plan.sortJson(),
                plan.limit(),
                plan.pipelineJsonStages()
        );
    }

    private static String detectPreferredCollection(String lower, List<String> availableCollections) {
        if (looksLikeScheduleRequest(lower)) {
            String scheduleCollection = resolveKnownCollectionName(availableCollections, "schedule");
            if (scheduleCollection != null) {
                return scheduleCollection;
            }
            return "schedule";
        }

        boolean mentionsClassroom = containsAny(
                lower,
                "classroom",
                "room",
                "\u0430\u0443\u0434\u0438\u0442\u043E\u0440",
                "\u043A\u0430\u0431\u0456\u043D\u0435\u0442",
                "\u043A\u043B\u0430\u0441",
                "\u043A\u043B\u0430\u0441\u0438",
                "\u043A\u043B\u0430\u0441\u0456\u0432"
        );
        if (mentionsClassroom) {
            String classroomCollection = resolveKnownCollectionName(availableCollections, "classrooms");
            if (classroomCollection != null) {
                return classroomCollection;
            }
            return "classrooms";
        }

        return null;
    }

    private static boolean looksLikeScheduleRequest(String lower) {
        if (lower == null || lower.isBlank()) {
            return false;
        }

        boolean mentionsSchedule = containsAny(
                lower,
                "schedule",
                "timetable",
                "\u0440\u043E\u0437\u043A\u043B\u0430\u0434",
                "\u0440\u0430\u0441\u043F\u0438\u0441\u0430\u043D"
        );
        if (mentionsSchedule) {
            return true;
        }

        boolean mentionsLesson = containsAny(
                lower,
                "lesson",
                "\u0437\u0430\u043D\u044F\u0442",
                "\u0443\u0440\u043E\u043A",
                "\u043F\u0430\u0440\u0430"
        );
        boolean mentionsCount = containsAny(
                lower,
                "count",
                "how many",
                "total",
                "\u0441\u043A\u0456\u043B\u044C\u043A\u0438",
                "\u0432\u0441\u044C\u043E\u0433\u043E",
                "\u043A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C"
        );
        return mentionsLesson && mentionsCount;
    }

    private static String normalizeNaturalLanguageRequest(String request) {
        if (request == null) {
            return "";
        }
        String trimmed = request.trim();
        if (trimmed.isBlank()) {
            return trimmed;
        }
        return repairLikelyUtf8Mojibake(trimmed);
    }

    private static String repairLikelyUtf8Mojibake(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        if (!containsAny(value, "Ð", "Ñ", "Ã", "Â")) {
            return value;
        }

        String repaired = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        if (countCyrillicLetters(repaired) > countCyrillicLetters(value)) {
            return repaired;
        }
        return value;
    }

    private static int countCyrillicLetters(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if ((ch >= '\u0400' && ch <= '\u04FF') || (ch >= '\u0500' && ch <= '\u052F')) {
                count++;
            }
        }
        return count;
    }

    private static String resolveKnownCollectionName(List<String> availableCollections, String preferredName) {
        if (preferredName == null || preferredName.isBlank()) {
            return null;
        }
        if (availableCollections == null || availableCollections.isEmpty()) {
            return preferredName;
        }
        for (String name : availableCollections) {
            if (name != null && name.equalsIgnoreCase(preferredName)) {
                return name;
            }
        }
        return null;
    }

    private static boolean containsAny(String value, String... needles) {
        if (value == null || value.isBlank() || needles == null) {
            return false;
        }
        for (String needle : needles) {
            if (needle != null && !needle.isBlank() && value.contains(needle)) {
                return true;
            }
        }
        return false;
    }

    private static void printCollectionCorrectionNotice(MongoQueryPlan originalPlan, MongoQueryPlan finalPlan) {
        if (originalPlan == null || finalPlan == null) {
            return;
        }
        String before = originalPlan.collection();
        String after = finalPlan.collection();
        if (before == null) {
            before = "";
        }
        if (after == null) {
            after = "";
        }
        if (before.equalsIgnoreCase(after)) {
            return;
        }
        System.out.println("Collection corrected by local heuristic: " + before + " -> " + after);
    }

    private static void printExecutedQueryPlan(MongoQueryPlan plan) {
        if (plan == null) {
            return;
        }

        JSONObject result = new JSONObject()
                .put("collection", plan.collection())
                .put("operation", normalizeOperationForOutput(plan.operation()));

        String operation = plan.operation() == null ? "" : plan.operation().toLowerCase(Locale.ROOT);
        if ("aggregate".equals(operation)) {
            JSONArray pipeline = new JSONArray();
            for (String stage : plan.pipelineJsonStages()) {
                pipeline.put(new JSONObject(stage));
            }
            result.put("pipeline", pipeline);
        } else {
            result.put("filter", plan.filterDocument());
            plan.projectionDocument().ifPresent(doc -> result.put("projection", doc));
            plan.sortDocument().ifPresent(doc -> result.put("sort", doc));
            if ("find".equals(operation) && plan.limit() > 0) {
                result.put("limit", plan.limit());
            }
        }

        System.out.println("Mongo query plan executed locally:");
        System.out.println(result.toString(2));
    }

    private static String normalizeOperationForOutput(String operation) {
        if (operation == null) {
            return "find";
        }
        String normalized = operation.trim().toLowerCase(Locale.ROOT);
        if ("count".equals(normalized)) {
            return "countDocuments";
        }
        if ("countdocuments".equals(normalized)) {
            return "countDocuments";
        }
        return normalized;
    }

    private static void executeFind(MongoCollection<Document> collection, MongoQueryPlan plan) {
        var find = collection.find(plan.filterDocument());
        plan.projectionDocument().ifPresent(find::projection);
        plan.sortDocument().ifPresent(find::sort);
        if (plan.limit() > 0) {
            find.limit(plan.limit());
        }

        List<Document> docs = find.into(new ArrayList<>());
        JsonWriterSettings settings = JsonWriterSettings.builder().indent(true).build();
        System.out.println("Documents found: " + docs.size());
        for (Document doc : docs) {
            System.out.println(doc.toJson(settings));
        }
    }

    private static void executeCount(MongoCollection<Document> collection, MongoQueryPlan plan) {
        long count = collection.countDocuments(plan.filterDocument());
        System.out.println("Count: " + count);
    }

    private static void executeAggregate(MongoCollection<Document> collection, MongoQueryPlan plan) {
        if (plan.pipelineDocuments().isEmpty()) {
            throw new IllegalArgumentException("Aggregate operation requires a non-empty pipeline.");
        }

        List<Document> docs = collection.aggregate(plan.pipelineDocuments()).into(new ArrayList<>());
        JsonWriterSettings settings = JsonWriterSettings.builder().indent(true).build();
        System.out.println("Aggregation result count: " + docs.size());
        for (Document doc : docs) {
            System.out.println(doc.toJson(settings));
        }
    }
}
