package com.example.neo4jlab;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GroupRepository extends Neo4jRepository<Group, Long> {
}