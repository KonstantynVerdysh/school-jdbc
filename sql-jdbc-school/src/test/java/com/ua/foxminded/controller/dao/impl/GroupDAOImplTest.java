package com.ua.foxminded.controller.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

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

class GroupDAOImplTest {
    private static GroupDAO groupDAO;
    
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
        
        groupDAO = new GroupDAOImpl("test.properties");
        StudentDAO studentDAO = new StudentDAOImpl("test.properties");
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
    final void getByStudentCount_returnGroupWhenInputIsStudentCount() {
        Map<Group, Integer> actual = null;
        int input = 30;
        try {
            actual = groupDAO.getByStudentCount(input);
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
            actual = groupDAO.getByStudentCount(-100);
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(actual.size() == 0);
    }
}
