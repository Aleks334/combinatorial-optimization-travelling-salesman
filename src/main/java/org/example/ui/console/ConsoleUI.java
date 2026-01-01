package org.example.ui.console;

import org.example.application.port.*;
import org.example.ui.console.exception.InvalidInputException;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleUI implements ConsoleInput, ConsoleOutput, MessagePresenter, LogManager {
    private final Scanner scanner;
    private final StringBuilder logBuffer = new StringBuilder();

    public ConsoleUI(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    public ConsoleUI() {
        this(System.in);
    }


    public void displayWelcome() {
        String s = "+========================================================+" + System.lineSeparator()
                + "|      Travelling Salesman Problem (TSP) - Solver       |" + System.lineSeparator()
                + "+========================================================+";
        println(s);
    }

    public void displaySectionHeader(String title) {
        println("\n" + "=".repeat(60));
        println(title);
        println("=".repeat(60));
    }

    public void displayCoordinates(int totalCount, List<String> formattedCoordinates) {
        Objects.requireNonNull(formattedCoordinates, "Coordinates list cannot be null");
        if (formattedCoordinates.isEmpty()) {
            showWarning("No coordinates to display");
            return;
        }

        println("\nCity Coordinates:");
        for (int i = 0; i < formattedCoordinates.size(); i++) {
            printf("  %2d. %s%n", i + 1, formattedCoordinates.get(i));
        }

        if (totalCount > formattedCoordinates.size()) {
            println("  ... (and " + (totalCount - formattedCoordinates.size()) + " more)");
        }
    }

    public String getString(String prompt) throws InvalidInputException {
        print(prompt);
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            throw new InvalidInputException("enter a valid string", e);
        }
    }

    public String getStringUntilValid(String prompt) {
        while (true) {
            try {
                return getString(prompt);
            } catch (InvalidInputException e) {
                showWarning("Please enter a valid String.");
            }
        }
    }

    public int getIntUntilValid(String prompt) {
        while (true) {
            try {
                return getInt(prompt);
            } catch (InvalidInputException e) {
                showWarning("Please enter a valid number.");
            }
        }
    }


    public int getInt(String prompt) throws InvalidInputException {
        print(prompt);
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (Exception e) {
            throw new InvalidInputException("enter a valid integer", e);
        }
    }

    public void close() {
        scanner.close();
    }

    public void showSuccess(String message) {
        println("[OK] " + message);
    }

    public void showError(String message) {
        println("[ERROR] " + message);
    }

    public void showWarning(String message) {
        println("[WARNING] " + message);
    }

    public void showInfo(String message) {
        println(message);
    }

    public void print(String message) {
        System.out.print(message);
        logBuffer.append(message);
        logBuffer.append(System.lineSeparator());
    }

    public void println(String message) {
        System.out.println(message);
        logBuffer.append(message);
        logBuffer.append(System.lineSeparator());
    }

    public void printf(String format, Object... args) {
        String formatted = String.format(format, args);
        System.out.print(formatted);
        logBuffer.append(formatted);
        logBuffer.append(System.lineSeparator());
    }

    public String getLogs() {
        return logBuffer.toString();
    }

    public void clearLogs() {
        logBuffer.setLength(0);
    }
}
