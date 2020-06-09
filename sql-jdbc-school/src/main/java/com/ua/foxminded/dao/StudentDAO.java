package com.ua.foxminded.dao;

import java.util.List;
import java.util.Map;

import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Student;

public interface StudentDAO {
    void insert(Student student) throws DAOException;
    void insert(List<Student> students) throws DAOException;
    void deleteById(int id) throws DAOException;
    List<Student> getByCourseName(String courseName) throws DAOException;
    void assignToCourse(int studentId, int courseId) throws DAOException;
    void assignToCourse(Map<Student, List<Course>> students) throws DAOException;
    void deleteFromCourse(int studentId, int courseId) throws DAOException;
    List<Student> showAll() throws DAOException;
}
