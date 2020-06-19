package com.ua.foxminded.controller.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

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

class GroupDAOImplTest {
    private static ScriptExecutor scriptExec = new ScriptExecutor();
    private static SchoolManager manager = new SchoolManager();
    
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
    final void getByStudentCount_returnGroupWhenInputIsStudentCount() {
        Map<Group, Integer> actual = null;
        int input = 30;
        try {
            actual = manager.getGroupsByStudentCount(input);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        
        assertTrue(actual.size() > 5);
        for (Map.Entry<Group, Integer> entry : actual.entrySet()) {
            assertTrue(input >= entry.getValue());
        }
    }
    
    @Test
    final void getByStudentCount_returnGroupWhenInputIsNotStudentCount() {
        Map<Group, Integer> actual = null;
        try {
            actual = manager.getGroupsByStudentCount(-100);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(actual.size() == 0);
    }
}
