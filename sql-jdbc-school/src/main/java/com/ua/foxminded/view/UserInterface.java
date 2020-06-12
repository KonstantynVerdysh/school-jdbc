package com.ua.foxminded.view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.ua.foxminded.controller.dao.CourseDAO;
import com.ua.foxminded.controller.dao.GroupDAO;
import com.ua.foxminded.controller.dao.StudentDAO;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
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
    
    public void runMenu() throws SchoolDAOException {
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
    
    private void removeStudentFromCourse() throws SchoolDAOException {
        int studentId = selectStudentToPrintCourses();
        List<Course> studentCourses = getStudentCourses(studentId);
        int courseId = selectCourse();
        if (getCoursesId(studentCourses).contains(courseId)) {
            studentDAO.deleteFromCourse(studentId, courseId);
            System.out.println("Student removed from the course success.");
        } else {
            System.out.println("Student havn't this course.");
        }
    }
    
    private void addStudentToCourse() throws SchoolDAOException {
        int studentId = selectStudentToPrintCourses();
        List<Course> studentCourses = getStudentCourses(studentId);
        int courseId = selectCourse(); 
        if (!getCoursesId(studentCourses).contains(courseId)) {
            studentDAO.assignToCourse(studentId, courseId);
            System.out.println("Student added to the course success.");
        } else {
            System.out.println("Student already on this course.");
        }
    }
    
    private int selectCourse() throws SchoolDAOException {
        List<Course> courses = courseDAO.showAll();
        printCourses(courses);
        System.out.println(UNDER_LINE);
        System.out.println("Please enter course to add.");
        System.out.println("course_id: ");
        return getTrueAmount(courses.size());
    }
    
    private int selectStudentToPrintCourses() throws SchoolDAOException {
        List<Student> students = studentDAO.showAll();
        printStudents(students);
        System.out.println(UNDER_LINE);
        System.out.println("Please enter student_id: ");
        return getTrueAmount(students.size());
    }
    
    private List<Course> getStudentCourses(int studentId) throws SchoolDAOException {
        List<Course> courses = courseDAO.getByStudentId(studentId);
        printCourses(courses);
        System.out.println(UNDER_LINE);
        return courses;
    }
    
    private void deleteStudent() throws SchoolDAOException {
        List<Student> students = studentDAO.showAll();
        printStudents(students);
        System.out.println("Please enter student_id to delete.");
        int studentId = getTrueAmount(students.size());
        studentDAO.deleteById(studentId);
        System.out.println("Student deleted success.");
    }
    
    private void addStudent() throws SchoolDAOException {
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
    
    private void findStudentsByCourse() throws SchoolDAOException {
        List<Course> courses = courseDAO.showAll();
        printCourses(courses);
        System.out.println(UNDER_LINE);
        System.out.println("Please enter course name for search: ");
        String courseName = getCourseName(courses);
        List<Student> students = studentDAO.getByCourseName(courseName);
        printStudents(students);
    }
    
    private String getCourseName(List<Course> courses) {
        List<String> coursesNames = getCoursesNames(courses);
        String result = "";
        while (true) {
            result = scanner.next();
            if (coursesNames.contains(result))
                break;
            System.out.println("Incorrect course.");
        }
        return result;
    }
    
    private void findGroups() throws SchoolDAOException {
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
    
    private int getTrueAmount(int maxSize) {
        int result = 0;
        while (true) {
            result = getNumber();
            if (result <= maxSize && result > 0)
                break;
            System.out.println("Incorrect id.");
        }
        return result;
    }
    
    private List<Integer> getCoursesId(List<Course> course) {
        return course.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }
    
    private List<String> getCoursesNames(List<Course> course) {
        return course.stream()
                .map(Course::getName)
                .collect(Collectors.toList());
    }
}
