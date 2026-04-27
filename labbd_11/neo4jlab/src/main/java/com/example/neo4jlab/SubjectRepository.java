package com.example.neo4jlab;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SubjectRepository extends Neo4jRepository<Subject, Long> {
}