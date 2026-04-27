package com.example.neo4jlab;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface TeacherRepository extends Neo4jRepository<Teacher, Long> {

    @Query("MATCH (t:Teacher)-[:TEACHES]->(s) RETURN t, s")
    List<Teacher> getTeachersWithSubjects();
}