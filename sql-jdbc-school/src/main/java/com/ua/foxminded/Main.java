package com.ua.foxminded;

import com.ua.foxminded.controller.ApplicationRunner;
import com.ua.foxminded.controller.SqlScriptExecutor;

public class Main { 
    private static final String CREATE_DB = "createDB.sql";
    private static final String CREATE_TABLES = "createTables.sql";
    private static final String DELETE_DB = "deleteDB.sql";
    
    public static void main(String[] args) {
        SqlScriptExecutor scriptExec = new SqlScriptExecutor();

        // create database
        scriptExec.execute(CREATE_DB);
        
        // create tables
        scriptExec.execute(CREATE_TABLES);

        ApplicationRunner runner = new ApplicationRunner();
        runner.runApp();
        
        // drop database
//        scriptExec.execute(POSTGRES_PROP, DELETE_DB);
    }
}
