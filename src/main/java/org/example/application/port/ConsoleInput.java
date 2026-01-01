package org.example.application.port;

import org.example.ui.console.exception.InvalidInputException;

public interface ConsoleInput {
    String getString(String prompt) throws InvalidInputException;
    int getInt(String prompt)  throws InvalidInputException;
    String getStringUntilValid(String prompt);
    int getIntUntilValid(String prompt);
}

