package com.ua.foxminded.view;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;

public class ConsoleIO {
    private Scanner scanner;
    
    public ConsoleIO() {
        scanner = new Scanner(System.in);
    }

    public String getLetterInput() {
        return scanner.next();
    }
    
    public int getNumberInput() throws SchoolDAOException {
        int number = 0;
        while (number == 0) {
            try {
                number = Integer.parseInt(scanner.next());
            } catch (Exception e) {
                throw new SchoolDAOException("Please enter a number.");
            }
        }
        return number;
    }
    
    public String getCourseNameInput(List<Course> courses) throws SchoolDAOException {
        List<String> coursesNames = getCourseNameList(courses);
        String result = "";
        while (true) {
            result = scanner.next();
            if (coursesNames.contains(result))
                break;
            throw new SchoolDAOException("Incorrect course.");
        }
        return result;
    }
        
    public String getStringInput() {
        return scanner.next();
    }
    
    public void close() {
        scanner.close();
    }
    
    private List<String> getCourseNameList(List<Course> course) {
        return course.stream()
                .map(Course::getName)
                .collect(Collectors.toList());
    }
}
