package org.example.ui.console;

import org.example.application.port.in.InputPort;
import org.example.application.port.out.OutputPort;
import org.example.infrastructure.logging.InMemoryLogManager;

import java.util.List;
import java.util.Scanner;

public class ConsoleAdapter implements InputPort, OutputPort {
    private final Scanner scanner = new Scanner(System.in);
    private final InMemoryLogManager logManager = new InMemoryLogManager();

    @Override
    public int getIntUntilValid(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                logInput(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    @Override
    public void println(String message) {
        System.out.println(message);
        logManager.appendLine(message);
    }

    @Override
    public void printf(String format, Object... args) {
        String msg = String.format(format, args);

        System.out.print(msg);
        logManager.append(msg);
    }

    @Override
    public void displayWelcome() {
        println("=== TSP Solver Application ===");
    }

    @Override
    public void displayHeader(String title) {
        println("\n--- " + title + " ---");
    }

    @Override
    public void displayError(String message) {
        println("[ERROR]: " + message);
    }

    @Override
    public void displaySuccess(String message) {
        println("[SUCCESS]: " + message);
    }

    @Override
    public void displayPoints(List<String> coordinates) {
        println("Points Preview:");
        coordinates.forEach(this::println);
    }

    @Override
    public String getLogs() {
        return logManager.getLogs();
    }

    private void logInput(String input) {
        logManager.appendLine("[USER INPUT]: " + input);
    }
}