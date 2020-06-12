package com.ua.foxminded.dao;

import java.util.List;

import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Student;

public interface StudentDAO {
    void insert(Student student) throws DAOException;
    void insert(List<Student> students) throws DAOException;
    void deleteById(int id) throws DAOException;
    List<Student> getByCourseName(String courseName) throws DAOException;
    void assignToCourse(int studentId, int courseId) throws DAOException;
    void deleteFromCourse(int studentId, int courseId) throws DAOException;
    List<Student> showAll() throws DAOException;
    void assignToCourse(List<Student> students) throws DAOException;
}
