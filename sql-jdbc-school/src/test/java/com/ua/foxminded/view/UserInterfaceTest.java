package com.ua.foxminded.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.controller.DataGenerator;
import com.ua.foxminded.controller.SqlScriptExecutor;
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

class UserInterfaceTest {
    private static UserInterface ui = new UserInterface();
    
    @BeforeAll
    public static void before() {
        SqlScriptExecutor scriptExec = new SqlScriptExecutor();
        scriptExec.execute("test.properties", "createTables.sql");
        
        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.getStudents();
        List<Group> groups = generator.getGroups();
        List<Course> courses = generator.getCourses();
        generator.relateStudentsToGroups(students, groups);
        generator.relateStudentsToCourses(students, courses);
        
        GroupDAO groupDAO = new GroupDAOImpl("test.properties");
        StudentDAO studentDAO = new StudentDAOImpl("test.properties");
        CourseDAO courseDAO = new CourseDAOImpl("test.properties");
        
        ui = new UserInterface();
        ui.setCourseDAO(courseDAO);
        ui.setGroupDAO(groupDAO);
        ui.setStudentDAO(studentDAO);

        try {
            courseDAO.create(courses);
            groupDAO.create(groups);
            studentDAO.insert(students);
            studentDAO.assignToCourse(students);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    final void findGroups_returnAllGroupNamesWhenInputIsMaxCountOfStudents() {
        String actual = "";
        try {
            actual = ui.findGroups(30);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }

        assertTrue(actual.length() > 50);
        assertTrue(actual.charAt(2) == '-');
        assertTrue(actual.charAt(5) == ' ');
        assertTrue(actual.charAt(6) == ':');
        assertTrue(actual.charAt(7) == ' ');
    }
    
    @Test
    final void findGroups_returnEmptyStringWhenInputIsSmallerThanMinGroupCount() {
        String actual = "";
        try {
            actual = ui.findGroups(-22);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }

        assertTrue(actual.length() == 0);
    }

    @Test
    final void findStudentsByCourse_returnStudentsByCourseWhenInputIsCourseName() {
        String actual = "";
        try {
            actual = ui.findStudentsByCourse("Political");
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        
        assertTrue(actual.length() > 200);
        assertTrue(actual.charAt(1) == '.' || actual.charAt(2) == '.');
        assertTrue(actual.charAt(2) == ' ' || actual.charAt(3) == ' ');
    }
    
    @Test
    final void findStudentsByCourse_returnEmptyStringWhenInputIsNotCourseName() {
        String actual = "";
        try {
            actual = ui.findStudentsByCourse("Party");
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        
        assertTrue(actual.length() == 0);
    }

    @Test
    final void testAddStudent() {
        String actual = "";
        try {
            actual = ui.addStudent("Bill", "Klinton");
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        String expected = "New student Bill Klinton added success.";

        assertEquals(expected, actual);
    }

    @Test
    final void deleteStudent_returnStringWhenInputIsStudentId() {
        String actual = "";
        try {
            actual = ui.deleteStudent(7);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        String expected = "Student deleted success.";
        
        assertEquals(expected, actual);
    }

    @Test
    final void addStudentToCourse_returnStringWhenInputIsStudentIdAndCourseId() {
        String actual = "";
        try {
            actual = ui.addStudentToCourse(15, 5);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        String expected = "Student added to the course success.";
        
        assertEquals(expected, actual);
    }
    
    @Test
    final void addStudentToCourse_returnExceptionWhenInputIsNotStudentIdAndOrNotCourseId() {  
        SchoolDAOException myException = assertThrows(SchoolDAOException.class, 
                () -> ui.addStudentToCourse(-1245, 1000));
        assertTrue(myException.getMessage().contains("Can't assign relate for student and course."));
    }

    @Test
    final void removeStudentFromCourse() {
        SchoolDAOException myException = assertThrows(SchoolDAOException.class, 
                () -> ui.removeStudentFromCourse(-1245, 1000));
        assertTrue(myException.getMessage().contains("Student havn't this course."));
    }
}
