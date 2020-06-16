package com.ua.foxminded.controller.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.controller.DataGenerator;
import com.ua.foxminded.controller.SqlScriptExecutor;
import com.ua.foxminded.controller.dao.CourseDAO;
import com.ua.foxminded.controller.dao.GroupDAO;
import com.ua.foxminded.controller.dao.StudentDAO;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;
import com.ua.foxminded.view.UserInterface;

class StudentDAOImplTest {
    private static StudentDAO studentDAO;
    
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
        studentDAO = new StudentDAOImpl("test.properties");
        CourseDAO courseDAO = new CourseDAOImpl("test.properties");
        
        UserInterface ui = new UserInterface();
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
    final void testGetByCourseName() {
        List<Student> actual = null;
        try {
            actual = studentDAO.getByCourseName("Biology");
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        List<String> expected = Arrays.asList("Andrew", "Robert", "Mike", "Diego", "Fred", "Lenny", "Stephan", "Milko", "Roberto",
            "Frank", "Kirk", "Kasper", "Kate", "Lili", "Damistas", "Lory", "Menny", "Brian", "Bondie", "Dyondi");
        
        for (Student s : actual) {
            assertTrue(expected.contains(s.getFirstName()));
        }
        assertTrue(actual.size() > 20);
    }

    @Test
    final void testGetByCourseNames() {
        List<Student> actual = null;
        try {
            actual = studentDAO.getByCourseName("Biola");
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        
        assertTrue(actual.size() == 0);
    }
}
