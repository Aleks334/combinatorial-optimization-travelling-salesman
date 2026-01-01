package org.example.application.service;

import org.example.domain.algorithm.Algorithm;
import org.example.application.port.ConsoleInput;
import org.example.application.port.ConsoleOutput;
import org.example.application.port.MessagePresenter;
import org.example.ui.console.exception.InvalidInputException;

public class AlgorithmService {
    private final ConsoleOutput output;
    private final ConsoleInput input;
    private final MessagePresenter presenter;

    public AlgorithmService(ConsoleOutput output, ConsoleInput input, MessagePresenter presenter) {
        this.output = output;
        this.input = input;
        this.presenter = presenter;
    }

    public Algorithm selectAlgorithm() {
        presenter.displaySectionHeader("Choose Algorithm");
        output.println("  1. Greedy Algorithm");
        output.println("  2. Ant Colony Optimization");

        int choice = input.getIntUntilValid("Choose algorithm (1-2): ");

        return switch (choice) {
            case 1 -> Algorithm.GREEDY;
            case 2 -> Algorithm.ANT_COLONY;
            default -> {
                presenter.showWarning("Invalid choice, defaulting to Ant Colony");
                yield Algorithm.ANT_COLONY;
            }
        };
    }
}
