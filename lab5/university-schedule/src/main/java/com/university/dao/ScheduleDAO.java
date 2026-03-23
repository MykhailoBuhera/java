package com.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.university.DBConnection;

public class ScheduleDAO {

    public void getScheduleByGroup(int groupId) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = """
                SELECT s.id_record, s.day_of_week,
                       t.surname, sub.subject_name,
                       r.room_number, ts.start_time, ts.end_time
                FROM schedule s
                JOIN teacher t ON s.id_teacher = t.id_teacher
                JOIN subject sub ON s.id_subject = sub.id_subject
                JOIN classroom r ON s.id_room = r.id_room
                JOIN time_slot ts ON s.id_slot = ts.id_slot
                WHERE s.id_group = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, groupId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id_record") + " | " +
                    rs.getString("day_of_week") + " | " +
                    rs.getString("surname") + " | " +
                    rs.getString("subject_name") + " | " +
                    rs.getString("room_number") + " | " +
                    rs.getTime("start_time") + "-" +
                    rs.getTime("end_time")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}