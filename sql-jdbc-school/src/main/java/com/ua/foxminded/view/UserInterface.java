package com.ua.foxminded.view;

import java.util.List;
import java.util.Map;

import com.ua.foxminded.controller.SchoolManager;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;

public class UserInterface {
    private static final String UNDER_LINE = "===============================";
    private SchoolManager manager;
    private ConsoleIO consoleIO;
    
    public UserInterface(SchoolManager manager, ConsoleIO consoleIO) {
        this.manager = manager;
        this.consoleIO = consoleIO;
    }

    public void runMenu() {
        boolean exit = false;

        while (!exit) {
            exit = getMenu();
        }
        consoleIO.close();
    }
    
    public boolean getMenu() {
        printMenu();
        try {
            switch (consoleIO.getLetterInput()) {
            case "a":
                print("Please enter student count for search: ");
                int studentCount = consoleIO.getNumberInput();
                Map<Group, Integer> groups = manager.getGroupsByStudentCount(studentCount);
                print(generateGroupsString(groups));
                break;
            case "b":
                List<Course> courses = manager.getCourses();
                printCourses(courses);
                print(UNDER_LINE);
                print("Please enter course name for search: ");
                String courseName = consoleIO.getCourseNameInput(courses);
                List<Student> students = manager.getStudentsByCourseName(courseName);
                print(generateStudentsString(students));
                break;
            case "c":
                print("Please enter student first_name: ");
                String name = consoleIO.getStringInput();
                print("Please enter student last_name: ");
                String lastName = consoleIO.getStringInput();
                Student student = new Student();
                student.setFirstName(name);
                student.setLastName(lastName);
                manager.addNewStudent(student);
                print("New student " + name + " " + lastName + " added success.");
                break;
            case "d":
                List<Student> students1 = manager.getStudents();
                print(generateStudentsString(students1));
                print("Please enter student_id to delete.");
                int studentId = getNumberByMaxSizeInput(students1.size());
                manager.deleteStudent(studentId);
                print("Student deleted success.");
                break;
            case "e":
                int studentId1 = getSelectedStudentId();
                printCoursesByStudentId(studentId1);
                int courseId = getSelectedCourseId();
                manager.addStudentToCourse(studentId1, courseId);
                print("Student added to the course success.");
                break;
            case "f":
                int studentId2 = getSelectedStudentId();
                printCoursesByStudentId(studentId2);
                int courseId1 = getSelectedCourseId();
                manager.removeStudentFromCourse(studentId2, courseId1);
                print("Student removed from the course success.");
                break;
            case "g":
                return true;
            default:
                print("Select a letter from 'a' to 'g'");
                break;
            }
        } catch (SchoolDAOException e) {
            print(e.getMessage());
        } catch (RuntimeException e) {
            print(e.getMessage());
        }
        return false;
    }
    
    public void printMenu() {
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
            result = consoleIO.getNumberInput();
            if (result <= maxSize && result > 0)
                break;
            throw new SchoolDAOException("Incorrect id.");
        }
        return result;
    }
    
    private void print(String toPrint) {
        System.out.println(toPrint);
    }
}
