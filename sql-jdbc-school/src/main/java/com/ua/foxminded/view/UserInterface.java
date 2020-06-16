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
    
    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void setStudentDAO(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void setCourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public void runMenu() {
        boolean exit = false;

        while (!exit) {
            printMenu();
            try {
                switch (scanner.next()) {
                case "a":
                    print("Please enter max student count for search: ");
                    int count = getInputNumber();
                    executeMenuItem(findGroups(count));
                    break;
                case "b":
                    List<Course> courses = courseDAO.showAll();
                    printCourses(courses);
                    print(UNDER_LINE);
                    print("Please enter course name for search: ");
                    String courseName = getInputCourseName(courses);
                    executeMenuItem(findStudentsByCourse(courseName));
                    break;
                case "c":
                    print("Please enter student first_name: ");
                    String name = scanner.next();
                    print("Please enter student last_name: ");
                    String lastName = scanner.next();
                    executeMenuItem(addStudent(name, lastName));
                    break;
                case "d":
                    List<Student> students = studentDAO.showAll();
                    print(getStudents(students));
                    print("Please enter student_id to delete.");
                    int studentId = getLimitedNumber(students.size());
                    executeMenuItem(deleteStudent(studentId));
                    break;
                case "e":
                    int studentId1 = selectStudent();
                    printStudentCourses(studentId1);
                    int courseId = selectCourse();
                    executeMenuItem(addStudentToCourse(studentId1, courseId));
                    break;
                case "f":
                    int studentId2 = selectStudent();
                    printStudentCourses(studentId2);
                    int courseId1 = selectCourse();
                    executeMenuItem(removeStudentFromCourse(studentId2, courseId1));
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
    
    public String findGroups(int count) throws SchoolDAOException {
        Map<Group, Integer> groups = groupDAO.getByStudentCount(count);
        return getGroupsStudentCount(groups);
    }
    
    public String findStudentsByCourse(String courseName) throws SchoolDAOException {
        List<Student> students = studentDAO.getByCourseName(courseName);
        return getStudents(students);
    }
    
    public String addStudent(String name, String lastName) throws SchoolDAOException {
        Student student = new Student();
        student.setFirstName(name);
        student.setLastName(lastName);
        studentDAO.insert(student);
        return "New student " + name + " " + lastName + " added success.";
    }
    
    public String deleteStudent(int studentId) throws SchoolDAOException {
        studentDAO.deleteById(studentId);
        return "Student deleted success.";
    }
    
    public String addStudentToCourse(int studentId, int courseId) throws SchoolDAOException {
        List<Course> studentCourses = getStudentCourses(studentId);
        if (!getCoursesId(studentCourses).contains(courseId)) {
            studentDAO.assignToCourse(studentId, courseId);
            return "Student added to the course success.";
        } else {
            throw new SchoolDAOException("Student already on this course.");
        }
    }
    
    public String removeStudentFromCourse(int studentId, int courseId) throws SchoolDAOException {       
        List<Course> studentCourses = getStudentCourses(studentId);
        if (getCoursesId(studentCourses).contains(courseId)) {
            studentDAO.deleteFromCourse(studentId, courseId);
            return "Student removed from the course success.";
        } else {
            throw new SchoolDAOException("Student havn't this course.");
        }
    }
    
    private int selectCourse() throws SchoolDAOException {
        List<Course> courses = courseDAO.showAll();
        printCourses(courses);
        print(UNDER_LINE);
        print("Please enter course to add.");
        print("course_id: ");
        return getLimitedNumber(courses.size());
    }
    
    private int selectStudent() throws SchoolDAOException {
        List<Student> students = studentDAO.showAll();
        print(getStudents(students));
        print(UNDER_LINE);
        print("Please enter student_id: ");
        return getLimitedNumber(students.get(students.size() - 1).getId());
    }
    
    private List<Course> getStudentCourses(int studentId) throws SchoolDAOException {
        return courseDAO.getByStudentId(studentId);
    }
    
    private void printStudentCourses(int studentId) throws SchoolDAOException {
        List<Course> courses = courseDAO.getByStudentId(studentId);
        printCourses(courses);
        print(UNDER_LINE);
    }
    
    private String getInputCourseName(List<Course> courses) throws SchoolDAOException {
        List<String> coursesNames = getCoursesName(courses);
        String result = "";
        while (true) {
            result = scanner.next();
            if (coursesNames.contains(result))
                break;
            throw new SchoolDAOException("Incorrect course.");
        }
        return result;
    }
    
    private int getInputNumber() throws SchoolDAOException {
        int number = 0;
        while (number == 0) {
            try {
                number = Integer.parseInt(scanner.next());
            } catch (Exception e) {
                throw new SchoolDAOException("It's not a number!");
            }
        }
        return number;
    }
    
    private void printCourses(List<Course> courses) {
        for (Course course : courses) {
            if (course.getDescription() != null) {
                print(course.getId() + ". " + course.getName() + ": " + course.getDescription());
            } else {
                print(course.getId() + ". " + course.getName());
            }
        }
    }
    
    private String getGroupsStudentCount(Map<Group, Integer> groups) {
        StringBuilder sBuilder = new StringBuilder();
        for (Map.Entry<Group, Integer> entry : groups.entrySet())
            sBuilder.append(entry.getKey().getName() + " : " + entry.getValue() + "\n");
        return sBuilder.toString();
    }
    
    private String getStudents(List<Student> students) {
        StringBuilder sBuilder = new StringBuilder();
        for (Student student : students) {
            sBuilder.append(student.getId() + ". " + student.getFirstName() + " " + student.getLastName() + "\n");
        }
        return sBuilder.toString();
    }
    
    private int getLimitedNumber(int maxSize) throws SchoolDAOException {
        int result = 0;
        while (true) {
            result = getInputNumber();
            if (result <= maxSize && result > 0)
                break;
            throw new SchoolDAOException("Incorrect id.");
        }
        return result;
    }
    
    private List<Integer> getCoursesId(List<Course> course) {
        return course.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }
    
    private List<String> getCoursesName(List<Course> course) {
        return course.stream()
                .map(Course::getName)
                .collect(Collectors.toList());
    }
    
    private void print(String toPrint) {
        System.out.println(toPrint);
    }
    
    private void executeMenuItem(String menuItem) {
        System.out.println(menuItem);
    }
}
