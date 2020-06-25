package com.ua.foxminded.controller.dao;

import java.util.List;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;

public interface CourseDAO {
    void createCourse(Course course) throws SchoolDAOException;
    void createCourses(List<Course> course) throws SchoolDAOException;
    List<Course> getCourses() throws SchoolDAOException;
    List<Course> getCoursesByStudentId(int studentId) throws SchoolDAOException;
}
