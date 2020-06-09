package com.ua.foxminded.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

class DataGeneratorTest {
    private DataGenerator generator = new DataGenerator();
    @Test
    public void getStudentsShouldReturnStudentList() {
        List<Student> actual = generator.getStudents();
        assertTrue(actual.size() == 200);
        
        Set<String> names = new HashSet<>();
        Set<String> lastNames = new HashSet<>();
        for (Student student : actual) {
            names.add(student.getFirstName());
            lastNames.add(student.getLastName());
        }
        assertTrue(names.contains("Roberto"));
        assertTrue(lastNames.contains("Linkoln"));
    }

    @Test
    public void getCoursesShouldReturnCourseList() {
        List<Course> actual = generator.getCourses();
        assertTrue(actual.size() == 10);
        
        Set<String> courses = new HashSet<>();
        Set<String> descriptions = new HashSet<>();
        for (Course course : actual) {
            courses.add(course.getName());
            descriptions.add(course.getDescription());
        }
        assertTrue(courses.contains("Biology"));
        assertTrue(descriptions.contains("Learn how to count apples"));
    }

    @Test
    public void getGroupsShouldReturnGroupList() {
        List<Group> actual = generator.getGroups();
        assertTrue(actual.size() == 10);
        
        String groupName = actual.get(0).getName();
        assertTrue(groupName.length() == 5);
        assertTrue(groupName.contains("-"));
    }

}
