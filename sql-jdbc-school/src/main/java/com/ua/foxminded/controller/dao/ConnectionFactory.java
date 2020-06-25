package com.ua.foxminded.controller.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import com.ua.foxminded.controller.PropertyReader;

public class ConnectionFactory {
    private ConnectionFactory() {}
    
    private static BasicDataSource dataSource = new BasicDataSource();
    private static final String POSTGRES_PROP = "db.properties";
    
    static {
        PropertyReader propReader = new PropertyReader();
        Properties properties = propReader.getProperties(POSTGRES_PROP);
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.user"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
