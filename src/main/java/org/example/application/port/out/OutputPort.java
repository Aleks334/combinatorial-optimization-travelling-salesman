package org.example.application.port.out;

import java.util.List;

public interface OutputPort {
    void println(String message);
    void printf(String format, Object... args);
    void displayWelcome();
    void displayHeader(String title);
    void displayError(String message);
    void displaySuccess(String message);
    void displayPoints(List<String> formattedCoordinates);
    String getLogs();
}

