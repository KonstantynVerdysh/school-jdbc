package com.ua.foxminded.view;

import static org.junit.jupiter.api.Assertions.*;

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

class UserInterfaceTest {
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
    public void runMenuTest() {
        
    }
}
