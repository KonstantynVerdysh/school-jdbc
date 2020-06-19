package com.ua.foxminded.controller;

import java.util.List;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;
import com.ua.foxminded.view.UserInterface;

public class ApplicationRunner {
    public void runApp() {
        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.getStudents();
        List<Group> groups = generator.getGroups();
        List<Course> courses = generator.getCourses();
        generator.relateStudentsToGroups(students, groups);
        generator.relateStudentsToCourses(students, courses);
        
        SchoolManager manager = new SchoolManager();
        UserInterface userInterface = new UserInterface(manager);

        try {
            manager.createGroups(groups);
            manager.createStudents(students);
            manager.createCourses(courses);
            manager.assignStudentsToCourse(students);
            userInterface.runMenu();
        } catch (SchoolDAOException e) {
            System.out.println(e.getMessage());
        }
    }
}
