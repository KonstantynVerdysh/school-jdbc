package com.ua.foxminded.controller.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import com.ua.foxminded.controller.PropertyReader;

public class ConnectionFactory {
    private ConnectionFactory() {}
    
    private static BasicDataSource dataSource = new BasicDataSource();
    private static final String URL = "db.url";
    private static final String USER_NAME = "db.user";
    private static final String PASSWORD = "db.password";
    
    static {
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }
    
    public static Connection getConnection(String propPath) throws SQLException {
        PropertyReader propReader = new PropertyReader();
        Properties properties = propReader.getProperties(propPath);
        dataSource.setUrl(properties.getProperty(URL));
        dataSource.setUsername(properties.getProperty(USER_NAME));
        dataSource.setPassword(properties.getProperty(PASSWORD));
        return dataSource.getConnection();
    }
}
