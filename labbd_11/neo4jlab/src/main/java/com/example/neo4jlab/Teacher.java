package com.example.neo4jlab;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Teacher {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String position;

    @Relationship(type = "TEACHES")
    private List<Subject> subjects = new ArrayList<>();

    public Teacher() {}

    public Teacher(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void addSubject(Subject s) {
        subjects.add(s);
    }

    public String toString() {
        return name + " (" + position + ")";
    }
}