package com.example.neo4jlab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Neo4jlabApplication {

    public static void main(String[] args) {
        SpringApplication.run(Neo4jlabApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(
            TeacherRepository teacherRepo,
            GroupRepository groupRepo,
            SubjectRepository subjectRepo) {

        return args -> {

            // CREATE
            Subject db = new Subject("Бази даних");
            Subject prog = new Subject("Програмування");

            subjectRepo.save(db);
            subjectRepo.save(prog);

            Teacher t1 = new Teacher("Іваненко", "доцент");
            t1.addSubject(db);

            Teacher t2 = new Teacher("Петренко", "професор");
            t2.addSubject(prog);

            teacherRepo.save(t1);
            teacherRepo.save(t2);

            Group g1 = new Group("КН-31");
            g1.addSubject(db);

            Group g2 = new Group("КН-32");
            g2.addSubject(prog);

            groupRepo.save(g1);
            groupRepo.save(g2);

            // READ
            System.out.println("=== ВСІ ВИКЛАДАЧІ ===");
            teacherRepo.findAll().forEach(System.out::println);

            // UPDATE
            t1 = teacherRepo.findAll().iterator().next();
            t1 = new Teacher("Іваненко Оновлений", "доцент");
            System.out.println("=== ОНОВЛЕНИЙ ВИКЛАДАЧ ===");
            System.out.println(t1);
            teacherRepo.save(t1);

            // CUSTOM QUERY
            System.out.println("=== ВИКЛАДАЧІ І ПРЕДМЕТИ ===");
            teacherRepo.getTeachersWithSubjects().forEach(System.out::println);

            // DELETE (можеш показати окремо)
            // teacherRepo.deleteAll();
        };
    }
}