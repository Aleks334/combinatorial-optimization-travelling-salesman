package org.example.application;

import org.example.domain.algorithm.Algorithm;
import org.example.ui.ConsoleUI;
import org.example.ui.InvalidMenuItemChoiceException;
import org.example.ui.Menu;

public class AlgorithmSelector {
    private final ConsoleUI ui;

    public AlgorithmSelector(ConsoleUI ui) {
        this.ui = ui;
    }

    public Algorithm selectAlgorithm() {
        ApplicationState state = new ApplicationState();

        Menu algorithmSelectionSubMenu = Menu.builder("Choose Algorithm")
                .addItem("Greedy Algorithm", () -> state.setSelectedAlgorithm(Algorithm.GREEDY))
                .addItem("Ant Colony Optimization", () -> state.setSelectedAlgorithm(Algorithm.ANT_COLONY))
                .build();

        algorithmSelectionSubMenu.display();
        int choice = ui.getChoice("Choose algorithm (1-2): ");

        try {
            algorithmSelectionSubMenu.handleChoice(choice);
            Algorithm selected = state.getSelectedAlgorithm();
            ui.showSuccess("Selected: " + selected.getDisplayName());
            return selected;
        } catch (InvalidMenuItemChoiceException e) {
            ui.showWarning("Invalid choice, defaulting to Ant Colony");
            return Algorithm.ANT_COLONY;
        }
    }
}

