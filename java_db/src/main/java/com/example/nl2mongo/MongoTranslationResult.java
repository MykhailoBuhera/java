package com.example.nl2mongo;

public record MongoTranslationResult(
        String rawJson,
        MongoQueryPlan queryPlan
) {
}
