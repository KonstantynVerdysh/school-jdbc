package com.ua.foxminded.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ua.foxminded.controller.SchoolManager;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;

class UserInterfaceTest {
    private SchoolManager mockManager = Mockito.mock(SchoolManager.class);
    private ConsoleIO mockConsoleIO = Mockito.mock(ConsoleIO.class);
    private UserInterface ui = new UserInterface(mockManager, mockConsoleIO);
    
    @Test
    public void getGroupsByStudentCount() throws SchoolDAOException {
        String expected = "Please enter student count for search: ";
        Mockito.when(mockConsoleIO.getLetterInput()).thenReturn("a");
        Mockito.when(mockConsoleIO.getNumberInput()).thenReturn(20);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ui.getMenu();
        System.setOut(null);
        
        Mockito.verify(mockConsoleIO).getLetterInput();
        Mockito.verify(mockConsoleIO).getNumberInput();
        
        String actual = out.toString();
        assertTrue(actual.contains(expected));
    }
}
