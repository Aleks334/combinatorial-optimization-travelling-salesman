package org.example.ui;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final StringBuilder logBuffer;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.logBuffer = new StringBuilder();
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
        if (formattedCoordinates == null || formattedCoordinates.isEmpty()) return;

        println("\nCity Coordinates:");
        for (int i = 0; i < formattedCoordinates.size(); i++) {
            printf("  %2d. %s%n", i + 1, formattedCoordinates.get(i));
        }

        if (totalCount > formattedCoordinates.size()) {
            println("  ... (and " + (totalCount - formattedCoordinates.size()) + " more)");
        }
    }

    public int getChoice(String prompt) {
        print(prompt);
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    public int getIntInput() {
        try {
            if (!scanner.hasNextLine()) {
                return -1;
            }
            String line = scanner.nextLine();
            return Integer.parseInt(line.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public void waitForEnter(String message) {
        print(message);
        try {
            scanner.nextLine();
        } catch (Exception ignored) {}
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
