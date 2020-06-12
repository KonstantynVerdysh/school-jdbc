package com.ua.foxminded.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.controller.PropertyReader;
import com.ua.foxminded.controller.dao.ConnectionFactory;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.controller.dao.impl.CourseDAOImpl;
import com.ua.foxminded.controller.dao.impl.GroupDAOImpl;
import com.ua.foxminded.controller.dao.impl.StudentDAOImpl;

class UserInterfaceTest {
    private PropertyReader propReader = new PropertyReader();
    private Properties postgres = propReader.getProperties("postgres.properties");
    private ConnectionFactory connectionFactory = new ConnectionFactory(postgres.getProperty("db.url"), postgres.getProperty("db.user"), postgres.getProperty("db.password"));
    private UserInterface ui = new UserInterface(new GroupDAOImpl(connectionFactory), new StudentDAOImpl(connectionFactory), new CourseDAOImpl(connectionFactory));
    private InputStream sInputStream = System.in;
    
    public void consoleInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
    
    @AfterEach
    public void after() {
        System.setIn(sInputStream);
    }
    
    @Test
    public void testRunMenu() {
        try {
            ui.runMenu();
            consoleInput("a");
            consoleInput("100");
        } catch (SchoolDAOException e) {
            e.printStackTrace();
        }
    }
}
