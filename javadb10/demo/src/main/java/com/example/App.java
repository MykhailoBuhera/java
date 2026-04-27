package com.example;

import java.util.Map;

import redis.clients.jedis.Jedis;

public class App {

    public static void main(String[] args) {

        try (Jedis jedis = new Jedis("localhost", 6379)) {

            // =========================
            // 🔹 CREATE (створення)
            // =========================

            jedis.hset("teacher:2", "name", "Ivan Petrenko");
            jedis.hset("teacher:2", "department", "IT");
            jedis.hset("teacher:2", "position", "Professor");

            jedis.hset("group:2", "number", "IPZ-243");
            jedis.hset("group:2", "students", "25");

            jedis.hset("subject:2", "name", "Databases");

            jedis.hset("schedule:2", "day", "Monday");
            jedis.hset("schedule:2", "teacher_id", "2");
            jedis.hset("schedule:2", "group_id", "2");
            jedis.hset("schedule:2", "subject_id", "2");

            System.out.println("CREATE done");

            // =========================
            // 🔹 READ (читання)
            // =========================

            Map<String, String> teacher = jedis.hgetAll("teacher:2");
            System.out.println("Teacher: " + teacher);

            Map<String, String> schedule = jedis.hgetAll("schedule:2");
            System.out.println("Schedule: " + schedule);

            // =========================
            // 🔹 UPDATE (оновлення)
            // =========================

            jedis.hset("teacher:2", "position", "Associate Professor");
            System.out.println("Updated Teacher: " + jedis.hgetAll("teacher:2"));

            // =========================
            // 🔹 DELETE (видалення)
            // =========================

            jedis.del("schedule:2");
            System.out.println("Schedule deleted");

            // =========================
            // 🔹 BONUS (зв’язки як в БД)
            // =========================

            // індекс: розклад викладача
            jedis.sadd("teacher:2:schedules", "2");

            System.out.println("Teacher schedules: " + jedis.smembers("teacher:2:schedules"));

        }
    }
}