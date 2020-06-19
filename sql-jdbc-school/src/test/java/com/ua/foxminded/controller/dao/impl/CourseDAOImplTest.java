package com.ua.foxminded.controller.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
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

class CourseDAOImplTest {
    private static CourseDAO courseDAO;
    private static SqlScriptExecutor scriptExec = new SqlScriptExecutor();
    
    @BeforeAll
    public static void before() {
        scriptExec.execute("test.properties", "createTables.sql");
        
        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.getStudents();
        List<Group> groups = generator.getGroups();
        List<Course> courses = generator.getCourses();
        generator.relateStudentsToGroups(students, groups);
        generator.relateStudentsToCourses(students, courses);
        
        GroupDAO groupDAO = new GroupDAOImpl();
        StudentDAO studentDAO = new StudentDAOImpl();
        courseDAO = new CourseDAOImpl();
        
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
    
    @AfterAll
    public static void after() {
        scriptExec.execute("test.properties", "dropObjects.sql");
    }
    
    @Test
    final void getByStudentId_returnCourseListByStudentId() {
        List<Course> actual = null;
        try {
            actual = courseDAO.getByStudentId(10);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(actual.size() > 0);
        assertTrue(actual.size() <= 3);
        
        List<String> expected = new ArrayList<>();
        expected.add("Mathematics");
        expected.add("Physics");
        expected.add("Biology");
        expected.add("Chemistry");
        expected.add("Business");
        expected.add("Geography");
        expected.add("Astronomy");
        expected.add("Political");
        expected.add("History");
        expected.add("Literature");
        
        assertTrue(expected.contains(actual.get(0).getName()));
    }

    @Test
    final void getByStudentIdd_returnEmptyListIfInputIsNotStudentId() {
        List<Course> actual = null;
        try {
            actual = courseDAO.getByStudentId(-10);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(actual.size() == 0);
    }
}
