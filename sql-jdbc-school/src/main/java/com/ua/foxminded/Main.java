package com.ua.foxminded;

import com.ua.foxminded.controller.ApplicationRunner;
import com.ua.foxminded.controller.ScriptExecutor;

public class Main { 
    private static final String CREATE_TABLES = "createTables.sql";
    
    public static void main(String[] args) {
        ScriptExecutor scriptExec = new ScriptExecutor();

        // create tables
        scriptExec.execute(CREATE_TABLES);

        ApplicationRunner runner = new ApplicationRunner();
        runner.runApp();
    }
}
