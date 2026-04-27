package com.example.neo4jlab;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Subject {

    @Id @GeneratedValue
    private Long id;

    private String name;

    public Subject() {}

    public Subject(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}