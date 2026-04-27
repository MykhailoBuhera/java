package com.example.neo4jlab;

import org.springframework.data.neo4j.core.schema.*;
import java.util.*;

@Node
public class Group {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "STUDIES")
    private List<Subject> subjects = new ArrayList<>();

    public Group() {}

    public Group(String name) {
        this.name = name;
    }

    public void addSubject(Subject s) {
        subjects.add(s);
    }

    public String toString() {
        return name;
    }
}