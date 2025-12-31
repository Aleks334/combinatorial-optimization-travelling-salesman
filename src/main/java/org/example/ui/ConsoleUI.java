package org.example.ui;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void displayWelcome() {
        System.out.println("+========================================================+");
        System.out.println("|      Travelling Salesman Problem (TSP) - Solver       |");
        System.out.println("+========================================================+");
    }

    public void displaySectionHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60));
    }

    public void displayCoordinates(int totalCount, List<String> formattedCoordinates) {
        if (formattedCoordinates == null || formattedCoordinates.isEmpty()) return;

        System.out.println("\nCity Coordinates:");
        for (int i = 0; i < formattedCoordinates.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, formattedCoordinates.get(i));
        }

        if (totalCount > formattedCoordinates.size()) {
            System.out.println("  ... (and " + (totalCount - formattedCoordinates.size()) + " more)");
        }
    }

    public int getChoice(String prompt) {
        System.out.print(prompt);
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
        System.out.print(message);
        try {
            scanner.nextLine();
        } catch (Exception ignored) {}
    }

    public void close() {
        scanner.close();
    }

    public void showSuccess(String message) {
        System.out.println("\n[OK] " + message);
    }

    public void showError(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    public void showWarning(String message) {
        System.out.println("\n[WARNING] " + message);
    }

    public void showInfo(String message) {
        System.out.println("\n" + message);
    }

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}

