package com.ua.foxminded.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ua.foxminded.dao.ConnectionFactory;
import com.ua.foxminded.dao.StudentDAO;
import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Student;

public class StudentDAOImpl implements StudentDAO {
    private ConnectionFactory connectionFactory;
    
    public StudentDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void insert(Student student) throws DAOException {
        String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?);";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, student.getFirstName());
            pStatement.setString(2, student.getLastName());
            pStatement.addBatch();
            pStatement.execute();
        } catch (SQLException e) {
            throw new DAOException("Can't write student.");
        }
    }

    @Override
    public void insert(List<Student> students) throws DAOException {
        String sql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Student student : students) {
                if (student.getGroupId() != 0) {
                    pStatement.setInt(1, student.getGroupId());
                    pStatement.setString(2, student.getFirstName());
                    pStatement.setString(3, student.getLastName());
                    pStatement.execute();
                } else {
                    insert(student);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Can't write student.");
        }
    }

    @Override
    public void deleteById(int studentId) throws DAOException {
        String sql = "DELETE FROM students WHERE student_id = ?;";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            pStatement.execute();
        } catch (SQLException e) {
            throw new DAOException("Can't delete student.");
        }
    }

    @Override
    public List<Student> getByCourseName(String courseName) throws DAOException {
        String sql = "SELECT s.student_id, s.group_id, s.first_name, s.last_name,"
                + " c.course_name FROM students_courses JOIN courses c USING (course_id) JOIN students"
                + " s USING (student_id) WHERE c.course_name = ?;";
        List<Student> result = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
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
            throw new DAOException("Can't get students by course.");
        }
        return result;
    }

    @Override
    public void assignToCourse(int studentId, int courseId) throws DAOException {
        String sql = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            pStatement.setInt(2, courseId);
            pStatement.execute();
        } catch (SQLException e) {
            throw new DAOException("Can't assign relate for student and course.");
        }
    }

    @Override
    public void assignToCourse(Map<Student, List<Course>> students) throws DAOException {
        String sql = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Map.Entry<Student, List<Course>> entry : students.entrySet()) {
                for (Course course : entry.getValue()) {
                    pStatement.setInt(1, entry.getKey().getId());
                    pStatement.setInt(2, course.getId());
                    pStatement.addBatch();
                }
            }
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Can't assign relate for student and course.");
        }
    }

    @Override
    public void deleteFromCourse(int studentId, int courseId) throws DAOException {
        String sql = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?;";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            pStatement.setInt(2, courseId);
            pStatement.execute();
        } catch (SQLException e) {
            throw new DAOException("Can't delete student from course");
        }
    }

    @Override
    public List<Student> showAll() throws DAOException {
        String sql = "SELECT student_id, group_id, first_name, last_name FROM students;";
        List<Student> result = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
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
            throw new DAOException("Can't read students.");
        }
        return result;
    }
}
