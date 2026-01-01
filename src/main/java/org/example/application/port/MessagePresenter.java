package org.example.application.port;

import java.util.List;

public interface MessagePresenter {
    void displayWelcome();
    void displaySectionHeader(String title);
    void displayCoordinates(int totalCount, List<String> formattedCoordinates);
    void showSuccess(String message);
    void showError(String message);
    void showWarning(String message);
    void showInfo(String message);
}

