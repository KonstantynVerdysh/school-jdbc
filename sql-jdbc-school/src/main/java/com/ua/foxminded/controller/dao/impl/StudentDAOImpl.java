package com.ua.foxminded.controller.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ua.foxminded.controller.dao.ConnectionFactory;
import com.ua.foxminded.controller.dao.StudentDAO;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Student;

public class StudentDAOImpl implements StudentDAO {
    
    @Override
    public void createStudent(Student student) throws SchoolDAOException {
        String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?);";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, student.getFirstName());
            pStatement.setString(2, student.getLastName());
            pStatement.addBatch();
            pStatement.execute();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't write student.");
        }
    }

    @Override
    public void createStudents(List<Student> students) throws SchoolDAOException {
        String sql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Student student : students) {
                if (student.getGroupId() != 0) {
                    pStatement.setInt(1, student.getGroupId());
                    pStatement.setString(2, student.getFirstName());
                    pStatement.setString(3, student.getLastName());
                    pStatement.execute();
                } else {
                    createStudent(student);
                }
            }
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't write student.");
        }
    }

    @Override
    public void deleteStudentById(int studentId) throws SchoolDAOException {
        String sql = "DELETE FROM students WHERE student_id = ?;";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            pStatement.execute();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't delete student.");
        }
    }

    @Override
    public List<Student> getStudentsByCourseName(String courseName) throws SchoolDAOException {
        String sql = "SELECT s.student_id, s.group_id, s.first_name, s.last_name,"
                + " c.course_name FROM students_courses JOIN courses c USING (course_id) JOIN students"
                + " s USING (student_id) WHERE c.course_name = ?;";
        List<Student> result = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, courseName);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getInt(1));
                    student.setGroupId(resultSet.getInt(2));
                    student.setFirstName(resultSet.getString(3));
                    student.setLastName(resultSet.getString(4));
                    result.add(student);
                }
            }
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't get students by course.");
        }
        return result;
    }

    @Override
    public void assignStudentsToCourse(int studentId, int courseId) throws SchoolDAOException {
        String sql = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            pStatement.setInt(2, courseId);
            pStatement.execute();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't assign relate for student and course.");
        }
    }

    @Override
    public void assignStudentsToCourse(List<Student> students) throws SchoolDAOException {
        String sql = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Student student : students) {
                for (Course course : student.getCourses()) {
                    pStatement.setInt(1, student.getId());
                    pStatement.setInt(2, course.getId());
                    pStatement.addBatch();
                }
            }
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't assign relate for student and course.");
        }
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) throws SchoolDAOException {
        String sql = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?;";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            pStatement.setInt(2, courseId);
            pStatement.execute();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't delete student from course");
        }
    }

    @Override
    public List<Student> getStudents() throws SchoolDAOException {
        String sql = "SELECT student_id, group_id, first_name, last_name FROM students;";
        List<Student> result = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql);
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setGroupId(resultSet.getInt(2));
                student.setFirstName(resultSet.getString(3));
                student.setLastName(resultSet.getString(4));
                result.add(student);
            }
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't read students.");
        }
        return result;
    }
}
