package com.ua.foxminded.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ua.foxminded.dao.ConnectionFactory;
import com.ua.foxminded.dao.CourseDAO;
import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Course;

public class CourseDAOImpl implements CourseDAO {
    private ConnectionFactory connectionFactory;
    
    public CourseDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void create(Course course) throws DAOException {
        String sql = "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, course.getName());
            pStatement.setString(2, course.getDescription());
            pStatement.addBatch();
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Can't write course.");
        } 
    }

    @Override
    public void create(List<Course> courses) throws DAOException {
        String sql = "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Course course : courses) {
                pStatement.setString(1, course.getName());
                pStatement.setString(2, course.getDescription());
                pStatement.addBatch();
            }
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Can't write courses.");
        } 
    }

    @Override
    public List<Course> showAll() throws DAOException {
        String sql = "SELECT course_id, course_name, course_description FROM courses;";
        List<Course> result = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql);
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt(1));
                course.setName(resultSet.getString(2));
                course.setDescription(resultSet.getString(3));
                result.add(course);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't read courses.");
        }
        return result;
    }

    @Override
    public List<Course> getByStudentId(int studentId) throws DAOException {
        String sql = "SELECT c.course_id, c.course_name FROM students_courses sc JOIN courses c USING (course_id) JOIN students s USING (student_id) WHERE sc.student_id = ?;";
        List<Course> result = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, studentId);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt(1));
                    course.setName(resultSet.getString(2));
                    result.add(course);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Can't get student by id.");
        }
        return result;
    }
}
