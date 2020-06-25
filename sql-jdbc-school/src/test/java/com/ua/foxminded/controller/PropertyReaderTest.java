package com.ua.foxminded.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.Test;

class PropertyReaderTest {
    PropertyReader propBuilder = new PropertyReader();
    @Test
    public void getPropertiesShouldReturnProperties() {
        Properties actual = propBuilder.getProperties("postgres.properties");
        assertTrue(actual.size() == 3);
        assertTrue(actual.containsKey("db.url"));
        assertTrue(actual.containsValue("jdbc:postgresql://localhost:5432/postgres"));
    }

}
