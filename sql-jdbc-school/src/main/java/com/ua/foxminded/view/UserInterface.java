package com.ua.foxminded.view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.ua.foxminded.controller.SchoolManager;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

public class UserInterface {
    private static final String UNDER_LINE = "===============================";
    private SchoolManager manager;
    private Scanner scanner;
    
    public UserInterface(SchoolManager manager) {
        this.manager = manager;
        scanner = new Scanner(System.in);
    }

    public void runMenu() {
        boolean exit = false;

        while (!exit) {
            printMenu();
            try {
                switch (scanner.next()) {
                case "a":
                    printGroupsByStudentCount();
                    break;
                case "b":
                    printStudentsByCourseName();
                    break;
                case "c":
                    addNewStudent();
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
                    print("Select a letter from 'a' to 'g'");
                    break;
                }
            } catch (SchoolDAOException e) {
                print(e.getMessage());
            } catch (Exception e) {
                print(e.getMessage());
            }
        }
        scanner.close();
    }
    
    private void printMenu() {
        print("");
        print("MENU:");
        print("a. Find all groups with less or equals student count");
        print("b. Find all students related to course with given name");
        print("c. Add new student");
        print("d. Delete student by STUDENT_ID");
        print("e. Add a student to the course");
        print("f. Remove the student from one of his or her courses");
        print("g. Exit");
    }
    
    public void printGroupsByStudentCount() throws SchoolDAOException {
        print("Please enter student count for search: ");
        int studentCount = getNumberInput();
        Map<Group, Integer> groups = manager.getGroupsByStudentCount(studentCount);
        print(generateGroupsString(groups));
    }
    
    public void printStudentsByCourseName() throws SchoolDAOException {
        List<Course> courses = manager.getCourses();
        printCourses(courses);
        print(UNDER_LINE);
        print("Please enter course name for search: ");
        String courseName = getCourseNameInput(courses);
        List<Student> students = manager.getStudentsByCourseName(courseName);
        print(generateStudentsString(students));
    }
    
    public void addNewStudent() throws SchoolDAOException {
        print("Please enter student first_name: ");
        String name = scanner.next();
        print("Please enter student last_name: ");
        String lastName = scanner.next();
        Student student = new Student();
        student.setFirstName(name);
        student.setLastName(lastName);
        manager.createStudent(student);
        print("New student " + name + " " + lastName + " added success.");
    }
    
    public void deleteStudent() throws SchoolDAOException {
        List<Student> students = manager.getStudents();
        print(generateStudentsString(students));
        print("Please enter student_id to delete.");
        int studentId = getNumberByMaxSizeInput(students.size());
        manager.deleteStudentById(studentId);
        print("Student deleted success.");
    }
    
    public void addStudentToCourse() throws SchoolDAOException {
        int studentId = getSelectedStudentId();
        printCoursesByStudentId(studentId);
        int courseId = getSelectedCourseId();
        List<Course> studentCourses = manager.getCoursesByStudentId(studentId);
        if (!getCourseIdList(studentCourses).contains(courseId)) {
            manager.assignStudentsToCourse(studentId, courseId);
            print("Student added to the course success.");
        } else {
            throw new SchoolDAOException("Student already on this course.");
        }
    }
    
    public void removeStudentFromCourse() throws SchoolDAOException {
        int studentId = getSelectedStudentId();
        printCoursesByStudentId(studentId);
        int courseId = getSelectedCourseId();
        List<Course> studentCourses = manager.getCoursesByStudentId(studentId);
        if (getCourseIdList(studentCourses).contains(courseId)) {
            manager.deleteStudentFromCourse(studentId, courseId);
            print("Student removed from the course success.");
        } else {
            throw new SchoolDAOException("Student havn't this course.");
        }
    }
    
    private int getSelectedCourseId() throws SchoolDAOException {
        List<Course> courses = manager.getCourses();
        printCourses(courses);
        print(UNDER_LINE);
        print("Please enter course_id to add: ");
        return getNumberByMaxSizeInput(courses.size());
    }
    
    private int getSelectedStudentId() throws SchoolDAOException {
        List<Student> students = manager.getStudents();
        print(generateStudentsString(students));
        print(UNDER_LINE);
        print("Please enter student_id: ");
        return getNumberByMaxSizeInput(students.get(students.size() - 1).getId());
    }
    
    private void printCoursesByStudentId(int studentId) throws SchoolDAOException {
        List<Course> courses = manager.getCoursesByStudentId(studentId);
        printCourses(courses);
        print(UNDER_LINE);
    }
    
    private String getCourseNameInput(List<Course> courses) throws SchoolDAOException {
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
    
    private int getNumberInput() throws SchoolDAOException {
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
    
    private void printCourses(List<Course> courses) {
        for (Course course : courses) {
            if (course.getDescription() != null) {
                print(String.format("%d. %s: %s", course.getId(), course.getName(), course.getDescription()));
            } else {
                print(String.format("%d. %s", course.getId(), course.getName()));
            }
        }
    }
    
    private String generateGroupsString(Map<Group, Integer> groups) {
        StringBuilder sBuilder = new StringBuilder();
        for (Map.Entry<Group, Integer> entry : groups.entrySet())
            sBuilder.append(String.format("%s : %d\n", entry.getKey().getName(), entry.getValue()));
        return sBuilder.toString();
    }
    
    private String generateStudentsString(List<Student> students) {
        StringBuilder sBuilder = new StringBuilder();
        for (Student student : students) {
            sBuilder.append(String.format("%d. %s %s\n", student.getId(), student.getFirstName(), student.getLastName()));
        }
        return sBuilder.toString();
    }
    
    private int getNumberByMaxSizeInput(int maxSize) throws SchoolDAOException {
        int result = 0;
        while (true) {
            result = getNumberInput();
            if (result <= maxSize && result > 0)
                break;
            throw new SchoolDAOException("Incorrect id.");
        }
        return result;
    }
    
    private List<Integer> getCourseIdList(List<Course> course) {
        return course.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }
    
    private List<String> getCourseNameList(List<Course> course) {
        return course.stream()
                .map(Course::getName)
                .collect(Collectors.toList());
    }
    
    private void print(String toPrint) {
        System.out.println(toPrint);
    }
}
