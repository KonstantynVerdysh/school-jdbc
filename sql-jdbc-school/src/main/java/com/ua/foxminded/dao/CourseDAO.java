package com.ua.foxminded.dao;

import java.util.List;

import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Course;

public interface CourseDAO {
    void create(Course course) throws DAOException;
    void create(List<Course> course) throws DAOException;
    List<Course> showAll() throws DAOException;
    List<Course> getByStudentId(int studentId) throws DAOException;
}
