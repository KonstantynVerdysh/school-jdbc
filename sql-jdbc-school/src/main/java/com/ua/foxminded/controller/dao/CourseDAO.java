package com.ua.foxminded.controller.dao;

import java.util.List;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;

public interface CourseDAO {
    void create(Course course) throws SchoolDAOException;
    void create(List<Course> course) throws SchoolDAOException;
    List<Course> showAll() throws SchoolDAOException;
    List<Course> getByStudentId(int studentId) throws SchoolDAOException;
}
