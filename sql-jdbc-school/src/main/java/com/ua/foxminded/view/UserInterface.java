package com.ua.foxminded.view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.ua.foxminded.dao.CourseDAO;
import com.ua.foxminded.dao.GroupDAO;
import com.ua.foxminded.dao.StudentDAO;
import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

public class UserInterface {
    private static final String UNDER_LINE = "===============================";
    private Scanner scanner;
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    
    public UserInterface(GroupDAO groupDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        scanner = new Scanner(System.in);
    }
    
    public void runMenu() throws DAOException {
        boolean exit = false;

        while (!exit) {
            printMenu();
            switch (scanner.next()) {
            case "a":
                findGroups();
                break;
            case "b":
                findStudentsByCourse();
                break;
            case "c":
                addStudent();
                break;
            case "d":
                deleteStudent();
                break;
            case "e":
                addStudentToCourse();
                break;
            case "f":
                removeStudentFromCourse();
                break;
            case "g":
                exit = true;
                break;
            default:
                System.out.println("Select a letter from 'a' to 'g'");
                break;
            }
        }
        scanner.close();
    }
    
    private void printMenu() {
        System.out.println();
        System.out.println("MENU:");
        System.out.println("a. Find all groups with less or equals student count");
        System.out.println("b. Find all students related to course with given name");
        System.out.println("c. Add new student");
        System.out.println("d. Delete student by STUDENT_ID");
        System.out.println("e. Add a student to the course");
        System.out.println("f. Remove the student from one of his or her courses");
        System.out.println("g. Exit");
    }
    
    private void removeStudentFromCourse() throws DAOException {
        int studentId = selectStudentToPrintCourses();
        int courseId = selectCourse();
        studentDAO.deleteFromCourse(studentId, courseId);
        System.out.println("Student removed from the course success.");
    }
    
    private void addStudentToCourse() throws DAOException {
        int studentId = selectStudentToPrintCourses();
        int courseId = selectCourse();
        studentDAO.assignToCourse(studentId, courseId);
        System.out.println("Student added to the course success.");
    }
    
    private int selectCourse() throws DAOException {
        List<Course> courses = courseDAO.showAll();
        printCourses(courses);
        System.out.println(UNDER_LINE);
        System.out.println("Please enter course to add.");
        System.out.println("course_id: ");
        return getNumber();
    }
    
    private int selectStudentToPrintCourses() throws DAOException {
        List<Student> students = studentDAO.showAll();
        printStudents(students);
        System.out.println(UNDER_LINE);
        System.out.println("Please enter student_id: ");
        int studentId = getNumber();
        List<Course> courses = courseDAO.getByStudentId(studentId);
        printCourses(courses);
        System.out.println(UNDER_LINE);
        return studentId;
    }
    
    private void deleteStudent() throws DAOException {
        List<Student> students = studentDAO.showAll();
        printStudents(students);
        System.out.println("Please enter student_id to delete.");
        int studentId = getNumber();
        studentDAO.deleteById(studentId);
        System.out.println("Student deleted success.");
    }
    
    private void addStudent() throws DAOException {
        System.out.println("Please enter student first_name: ");
        String name = scanner.next();
        System.out.println("Please enter student last_name: ");
        String lastName = scanner.next();
        Student student = new Student();
        student.setFirstName(name);
        student.setLastName(lastName);
        studentDAO.insert(student);
        System.out.println("New student " + name + " " + lastName + " added success.");
    }
    
    private void findStudentsByCourse() throws DAOException {
        List<Course> courses = courseDAO.showAll();
        printCourses(courses);
        System.out.println(UNDER_LINE);
        System.out.println("Please enter course name for search: ");
        String courseName = scanner.next();
        List<Student> students = studentDAO.getByCourseName(courseName);
        printStudents(students);
    }
    
    private void findGroups() throws DAOException {
        System.out.println("Please enter max student count for search: ");
        int count = getNumber();
        Map<Group, Integer> groups = groupDAO.getByStudentCount(count);
        printGroupsStudentCount(groups);
    }
    
    private int getNumber() {
        int number = 0;
        while (number == 0) {
            try {
                number = Integer.parseInt(scanner.next());
            } catch (Exception e) {
                System.out.println("It's not a number!");
            }
        }
        return number;
    }
    
    private void printCourses(List<Course> courses) {
        for (Course course : courses) {
            if (course.getDescription() != null) {
                System.out.println(course.getId() + ". " + course.getName() + ": " + course.getDescription());
            } else {
                System.out.println(course.getId() + ". " + course.getName());
            }
        }
    }
    
    private void printGroupsStudentCount(Map<Group, Integer> groups) {
        for (Map.Entry<Group, Integer> entry : groups.entrySet())
            System.out.println(entry.getKey().getName() + " : " + entry.getValue());
    }
    
    private void printStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println(student.getId() + ". " + student.getFirstName() + " " + student.getLastName());
        }
    }
}
