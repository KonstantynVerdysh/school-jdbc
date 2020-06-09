package com.ua.foxminded.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    public Properties getProperties(String config) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(config)) {
            if (input == null) {
                throw new IOException("File \"" + config + "\" not found");
            }
            Properties prop = new Properties();
            prop.load(input);
            return prop; 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new Properties();
    }
}
