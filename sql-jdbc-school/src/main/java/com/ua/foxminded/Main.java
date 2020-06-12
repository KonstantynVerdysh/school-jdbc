package com.ua.foxminded;

import java.util.Properties;

import com.ua.foxminded.controller.ApplicationRunner;
import com.ua.foxminded.controller.PropertyReader;
import com.ua.foxminded.controller.SqlScriptExecutor;
import com.ua.foxminded.controller.dao.ConnectionFactory;

public class Main {
    public static void main(String[] args) {
        final String url = "db.url";
        final String userName = "db.user";
        final String password = "db.password";
        
        PropertyReader propReader = new PropertyReader();
        Properties postgres = propReader.getProperties("postgres.properties");
        Properties user = propReader.getProperties("user.properties");
        
        ConnectionFactory postgresConnection = new ConnectionFactory(postgres.getProperty(url), postgres.getProperty(userName), postgres.getProperty(password));
        ConnectionFactory userConnection = new ConnectionFactory(user.getProperty(url), user.getProperty(userName), user.getProperty(password));
        
        SqlScriptExecutor scriptExec = new SqlScriptExecutor();
        
        // create database
        scriptExec.execute(postgresConnection, "createDB.sql");
        
        // create tables
        scriptExec.execute(userConnection, "createTables.sql");

        ApplicationRunner runner = new ApplicationRunner();
        runner.runApp(userConnection);
        
        // drop database
        scriptExec.execute(postgresConnection, "deleteDB.sql");
    }
}
