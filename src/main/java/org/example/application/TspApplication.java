package org.example.application;

import org.example.ui.ConsoleUI;
import org.example.ui.Menu;

public class TspApplication implements Application {
    private static final String FILE_NAME = "data.txt";

    private final ConsoleUI ui;
    private final DataManager dataManager;
    private final AlgorithmSelector algorithmSelector;
    private final TspRunner tspRunner;
    private final ApplicationState state;

    public TspApplication() {
        this.ui = new ConsoleUI();
        this.dataManager = new DataManager(FILE_NAME, ui);
        this.algorithmSelector = new AlgorithmSelector(ui);
        this.tspRunner = new TspRunner(ui);
        this.state = new ApplicationState();
    }

    public TspApplication(ConsoleUI ui, DataManager dataManager,
                          AlgorithmSelector algorithmSelector,
                          TspRunner tspRunner,
                          ApplicationState state) {
        this.ui = ui;
        this.dataManager = dataManager;
        this.algorithmSelector = algorithmSelector;
        this.tspRunner = tspRunner;
        this.state = state;
    }

    public void run() {
        ui.displayWelcome();
        Menu mainMenu = createMainMenu();

        while (!state.shouldExit()) {
            mainMenu.display();
            int choice = ui.getChoice("Choose option (1-6): ");

            if (choice == -1) {
                ui.showWarning("Invalid input. Please enter a number.");
                continue;
            }

            try {
                mainMenu.handleChoice(choice);
            } catch (Exception e) {
                ui.showWarning("Invalid choice. Please try again.");
            }
        }

        terminate();
    }

    public void terminate() {
        ui.close();
        System.exit(0);
    }

    private Menu createMainMenu() {
        return Menu.builder("Main Menu")
                .addItem("Load data from file (" + FILE_NAME + ")",
                        this::loadDataFromFile)
                .addItem("Generate new random data",
                        this::generateRandomData)
                .addItem("Generate and save data to file",
                        this::generateAndSaveData)
                .addItem("Choose algorithm",
                        this::chooseAlgorithm)
                .addItem("Solve TSP",
                        this::solveTsp)
                .addItem("Exit",
                        this::prepareForExit)
                .build();
    }

    private void loadDataFromFile() {
        state.setPoints(dataManager.loadFromFile());
    }

    private void generateRandomData() {
        ui.print("\nEnter number of cities to generate (or 0 for random 1-50): ");
        int count = ui.getIntInput();
        state.setPoints(dataManager.generate(count));
    }

    private void generateAndSaveData() {
        ui.print("\nEnter number of cities to generate (or 0 for random 1-50): ");
        int count = ui.getIntInput();
        state.setPoints(dataManager.generateAndSave(count));
    }

    private void chooseAlgorithm() {
        state.setSelectedAlgorithm(algorithmSelector.selectAlgorithm());
    }

    private void solveTsp() {
        if (state.hasPoints()) {
            tspRunner.solve(state.getPoints(), state.getSelectedAlgorithm());
        } else {
            ui.showWarning("Please load or generate data first (option 1, 2 or 3)");
        }
    }

    private void prepareForExit() {
        ui.showInfo("Goodbye!");
        state.exit();
    }
}

