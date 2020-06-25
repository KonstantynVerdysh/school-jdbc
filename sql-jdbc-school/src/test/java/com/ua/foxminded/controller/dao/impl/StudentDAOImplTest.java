package com.ua.foxminded.controller.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.controller.DataGenerator;
import com.ua.foxminded.controller.SchoolManager;
import com.ua.foxminded.controller.ScriptExecutor;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

class StudentDAOImplTest {
    private static ScriptExecutor scriptExec = new ScriptExecutor();
    private static SchoolManager manager = new SchoolManager(new GroupDAOImpl(), new StudentDAOImpl(), new CourseDAOImpl());
    
    @BeforeAll
    public static void before() {
        scriptExec.execute("createTables.sql");
        
        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.getStudents();
        List<Group> groups = generator.getGroups();
        List<Course> courses = generator.getCourses();
        generator.relateStudentsToGroups(students, groups);
        generator.relateStudentsToCourses(students, courses);
        
        try {
            manager.createCourses(courses);
            manager.createGroups(groups);
            manager.createStudents(students);
            manager.assignStudentsToCourse(students);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @AfterAll
    public static void after() {
        scriptExec.execute("dropObjects.sql");
    }

    @Test
    final void testGetByCourseName() {
        List<Student> actual = null;
        try {
            actual = manager.getStudentsByCourseName("Biology");
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
            actual = manager.getStudentsByCourseName("Biola");
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        
        assertTrue(actual.size() == 0);
    }
}
