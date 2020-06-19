package com.ua.foxminded.controller;

import java.util.List;
import java.util.Map;

import com.ua.foxminded.controller.dao.CourseDAO;
import com.ua.foxminded.controller.dao.GroupDAO;
import com.ua.foxminded.controller.dao.StudentDAO;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.controller.dao.impl.CourseDAOImpl;
import com.ua.foxminded.controller.dao.impl.GroupDAOImpl;
import com.ua.foxminded.controller.dao.impl.StudentDAOImpl;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

public class SchoolManager {
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    public SchoolManager() {
        groupDAO = new GroupDAOImpl();
        studentDAO = new StudentDAOImpl();
        courseDAO = new CourseDAOImpl();
    }

    public void createGroups(List<Group> groups) throws SchoolDAOException {
        groupDAO.createGroups(groups);
    }
    
    public Map<Group, Integer> getGroupsByStudentCount(int studentCount) throws SchoolDAOException {
        return groupDAO.getGroupsByStudentCount(studentCount);
    }
    
    public List<Group> getGroups() throws SchoolDAOException {
        return groupDAO.getGroups();
    }
    
    public void createCourses(List<Course> courses) throws SchoolDAOException {
        courseDAO.createCourses(courses);
    }
    
    public List<Course> getCourses() throws SchoolDAOException {
        return courseDAO.getCourses();
    }
    
    public List<Course> getCoursesByStudentId(int studentId) throws SchoolDAOException {
        return courseDAO.getCoursesByStudentId(studentId);
    }
    
    public void createStudent(Student student) throws SchoolDAOException {
        studentDAO.createStudent(student);
    }
    
    public void createStudents(List<Student> students) throws SchoolDAOException {
        studentDAO.createStudents(students);
    }
    
    public void deleteStudentById(int studentId) throws SchoolDAOException {
        studentDAO.deleteStudentById(studentId);
    }
    
    public List<Student> getStudentsByCourseName(String courseName) throws SchoolDAOException {
        return studentDAO.getStudentsByCourseName(courseName);
    }
    
    public void assignStudentsToCourse(int studentId, int courseId) throws SchoolDAOException {
        studentDAO.assignStudentsToCourse(studentId, courseId);
    }
    
    public void assignStudentsToCourse(List<Student> students) throws SchoolDAOException {
        studentDAO.assignStudentsToCourse(students);
    }
    
    public void deleteStudentFromCourse(int studentId, int courseId) throws SchoolDAOException {
        studentDAO.deleteStudentFromCourse(studentId, courseId);
    }
    
    public List<Student> getStudents() throws SchoolDAOException {
        return studentDAO.getStudents();
    }
}
