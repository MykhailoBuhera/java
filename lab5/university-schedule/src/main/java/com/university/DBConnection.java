package com.university;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Properties props = new Properties();

            InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            props.load(input);

            Connection conn = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );

            System.out.println(" Connected to DB");

            return conn;

        } catch (Exception e) {
            e.printStackTrace();//вииводить потік помилок з назвою помилки і зарзу методом де це викликано
        }

        return null;
    }
}