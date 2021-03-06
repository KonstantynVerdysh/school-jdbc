package com.ua.foxminded.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ua.foxminded.controller.dao.CourseDAO;
import com.ua.foxminded.controller.dao.GroupDAO;
import com.ua.foxminded.controller.dao.StudentDAO;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

public class SchoolManager {
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    public SchoolManager(GroupDAO groupDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
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
    
    public void createStudents(List<Student> students) throws SchoolDAOException {
        studentDAO.createStudents(students);
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
   
    public boolean addNewStudent(Student student) throws SchoolDAOException {
        studentDAO.createStudent(student);
        return true;
    }
    
    public boolean deleteStudent(int studentId) throws SchoolDAOException {
        studentDAO.deleteStudentById(studentId);
        return true;
    }
    
    public boolean addStudentToCourse(int studentId, int courseId) throws SchoolDAOException {
        List<Course> studentCourses = getCoursesByStudentId(studentId);
        if (!getCourseIdList(studentCourses).contains(courseId)) {
            assignStudentsToCourse(studentId, courseId);
            return true;
        } else {
            throw new SchoolDAOException("Student already on this course.");
        }
    }
    
    public boolean removeStudentFromCourse(int studentId, int courseId) throws SchoolDAOException {
        List<Course> studentCourses = getCoursesByStudentId(studentId);
        if (getCourseIdList(studentCourses).contains(courseId)) {
            deleteStudentFromCourse(studentId, courseId);
            return true;
        } else {
            throw new SchoolDAOException("Student havn't this course.");
        }
    }
    
    private List<Integer> getCourseIdList(List<Course> course) {
        return course.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }
}
