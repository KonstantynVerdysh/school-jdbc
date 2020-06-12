package com.ua.foxminded.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ua.foxminded.dao.ConnectionFactory;

public class SqlScriptExecutor {
    public void execute(String url, String user, String password, String script) {
        ConnectionFactory connectionFactory = new ConnectionFactory(url, user, password);
        String filePath = "";
        try {
            if (getClass().getClassLoader().getResource(script) == null) {
                throw new IOException("File \"" + script + "\" not found");
            }
            filePath = getClass().getClassLoader().getResource(script).getFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             Connection connection = connectionFactory.getConnection();
            Statement statement = connection.createStatement()) {
            String line = null;
            StringBuilder query = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                query.append(line);
            }
            statement.execute(query.toString());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
