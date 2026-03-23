package com.university;
import com.university.dao.ScheduleDAO;
import com.university.dao.TeacherDAO;

public class App {
    public static void main(String[] args) {
        /*DBConnection.getConnection();
        TeacherDAO dao = new TeacherDAO();
        dao.getAllTeachers();
         dao.addTeacher("Shevchenko", "Taras"); 
        dao.getAllTeachers();*/
        
        TeacherDAO teacherDAO = new TeacherDAO();
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        // CRUD
       // teacherDAO.addTeacher("Test", "User");
        //teacherDAO.getAllTeachers();

       // teacherDAO.updateTeacher(1, "Updated", "Name");
       // teacherDAO.deleteTeacher(2);

        // Пошук
//teacherDAO.findTeacherBySurname("Shev");

        // Metadata
        teacherDAO.printWithMetadata();

        // Schedule (ГОЛОВНЕ)
      // scheduleDAO.getScheduleByGroup(1);



    }
}