package com.ua.foxminded.controller.dao;

import java.util.List;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Student;

public interface StudentDAO {
    void createStudent(Student student) throws SchoolDAOException;
    void createStudents(List<Student> students) throws SchoolDAOException;
    void deleteStudentById(int id) throws SchoolDAOException;
    List<Student> getStudentsByCourseName(String courseName) throws SchoolDAOException;
    void assignStudentsToCourse(int studentId, int courseId) throws SchoolDAOException;
    void deleteStudentFromCourse(int studentId, int courseId) throws SchoolDAOException;
    List<Student> getStudents() throws SchoolDAOException;
    void assignStudentsToCourse(List<Student> students) throws SchoolDAOException;
}
