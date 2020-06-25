package com.ua.foxminded.controller;

import java.util.List;

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
import com.ua.foxminded.view.ConsoleIO;
import com.ua.foxminded.view.UserInterface;

public class ApplicationRunner {
    public void runApp() {
        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.getStudents();
        List<Group> groups = generator.getGroups();
        List<Course> courses = generator.getCourses();
        generator.relateStudentsToGroups(students, groups);
        generator.relateStudentsToCourses(students, courses);
        
        GroupDAO groupDAO = new GroupDAOImpl();
        StudentDAO studentDAO = new StudentDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();
        
        SchoolManager manager = new SchoolManager(groupDAO, studentDAO, courseDAO);
        ConsoleIO consoleIO = new ConsoleIO();
        UserInterface userInterface = new UserInterface(manager, consoleIO);

        try {
            manager.createGroups(groups);
            manager.createStudents(students);
            manager.createCourses(courses);
            manager.assignStudentsToCourse(students);
            userInterface.runMenu();
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("System error.");
        }
    }
}
