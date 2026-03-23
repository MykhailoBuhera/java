package com.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.university.DBConnection;

public class TeacherDAO {

    public void getAllTeachers() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM teacher";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id_teacher") + " " +
                        rs.getString("surname") + " " +
                        rs.getString("first_name")
                );
            }
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTeacher(String surname, String firstName) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO teacher (surname, first_name) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, surname);
            ps.setString(2, firstName);

            ps.executeUpdate();

            System.out.println(" Teacher added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTeacher(int id, String surname, String firstName) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE teacher SET surname=?, first_name=? WHERE id_teacher=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, surname);
            ps.setString(2, firstName);
            ps.setInt(3, id);

            ps.executeUpdate();

            System.out.println("✅ Teacher updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTeacher(int id) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM teacher WHERE id_teacher=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("✅ Teacher deleted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findTeacherBySurname(String surname) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM teacher WHERE surname LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + surname + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id_teacher") + " " +
                    rs.getString("surname") + " " +
                    rs.getString("first_name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printWithMetadata() {//виводить всі дані з таблиці, не знаючи її структуру 
    try {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM teacher";
        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();

        int columns = meta.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columns; i++) {
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}