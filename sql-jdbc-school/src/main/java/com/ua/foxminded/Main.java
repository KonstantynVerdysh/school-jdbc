package com.ua.foxminded;

import java.util.List;
import java.util.Properties;

import com.ua.foxminded.controller.DataGenerator;
import com.ua.foxminded.controller.PropertyReader;
import com.ua.foxminded.controller.SqlScriptExecutor;
import com.ua.foxminded.dao.ConnectionFactory;
import com.ua.foxminded.dao.CourseDAO;
import com.ua.foxminded.dao.GroupDAO;
import com.ua.foxminded.dao.StudentDAO;
import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.dao.impl.CourseDAOImpl;
import com.ua.foxminded.dao.impl.GroupDAOImpl;
import com.ua.foxminded.dao.impl.StudentDAOImpl;
import com.ua.foxminded.model.Course;
import com.ua.foxminded.model.Group;
import com.ua.foxminded.model.Student;
import com.ua.foxminded.view.UserInterface;

public class Main {
    public static void main(String[] args) {
        final String url = "db.url";
        final String user = "db.user";
        final String password = "db.password";
        
        SqlScriptExecutor scriptExec = new SqlScriptExecutor();
        PropertyReader propReader = new PropertyReader();
        
        Properties postgres = propReader.getProperties("postgres.properties");
        Properties user1 = propReader.getProperties("user1.properties");
        
        // create database
        scriptExec.execute(postgres.getProperty(url), postgres.getProperty(user), postgres.getProperty(password), "createDB.sql");
        
        // create tables
        scriptExec.execute(user1.getProperty(url), user1.getProperty(user), user1.getProperty(password), "createTables.sql");

        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.getStudents();
        List<Group> groups = generator.getGroups();
        List<Course> courses = generator.getCourses();
        generator.relateStudentsToGroups(students, groups);
        
        ConnectionFactory connectionFactory = new ConnectionFactory(user1.getProperty(url), user1.getProperty(user), user1.getProperty(password));
        GroupDAO groupDAO = new GroupDAOImpl(connectionFactory);
        StudentDAO studentDAO = new StudentDAOImpl(connectionFactory);
        CourseDAO courseDAO = new CourseDAOImpl(connectionFactory);
        
        UserInterface userInterface = new UserInterface(groupDAO, studentDAO, courseDAO);

        try {
            groupDAO.create(groups);
            studentDAO.insert(students);
            courseDAO.create(courses);
            studentDAO.assignToCourse(generator.relateStudentsToCourses(students, courses));
            userInterface.runMenu();
        } catch (DAOException e) {
            System.out.println(e.getMessage());
        }
        
        // drop database
        scriptExec.execute(postgres.getProperty(url), postgres.getProperty(user), postgres.getProperty(password), "deleteDB.sql");
    }
}
