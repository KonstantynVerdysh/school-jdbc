package com.ua.foxminded.controller.dao;

import java.util.List;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Student;

public interface StudentDAO {
    void insert(Student student) throws SchoolDAOException;
    void insert(List<Student> students) throws SchoolDAOException;
    void deleteById(int id) throws SchoolDAOException;
    List<Student> getByCourseName(String courseName) throws SchoolDAOException;
    void assignToCourse(int studentId, int courseId) throws SchoolDAOException;
    void deleteFromCourse(int studentId, int courseId) throws SchoolDAOException;
    List<Student> showAll() throws SchoolDAOException;
    void assignToCourse(List<Student> students) throws SchoolDAOException;
}
