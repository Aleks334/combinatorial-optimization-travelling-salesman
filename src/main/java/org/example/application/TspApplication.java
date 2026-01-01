package org.example.application;

import org.example.ui.ConsoleUI;
import org.example.ui.Menu;
import org.example.infrastructure.OutputSaver;
import org.example.ui.DefaultChartCreator;
import org.example.ui.DefaultChartDisplayer;
import org.example.domain.model.Tour;

import java.io.File;

public class TspApplication implements Application {
    private final String FILE_NAME = "data.txt";
    private final String OUTPUT_DIR = "outputs";

    private final ConsoleUI ui;
    private final DataManager dataManager;
    private final AlgorithmSelector algorithmSelector;
    private final TspRunner tspRunner;
    private final ApplicationState state;
    private final OutputSaver outputSaver;

    public TspApplication() {
        this.ui = new ConsoleUI();
        this.dataManager = new DataManager(FILE_NAME, ui);
        this.algorithmSelector = new AlgorithmSelector(ui);
        this.tspRunner = new TspRunner(ui, new DefaultChartCreator(), new DefaultChartDisplayer());
        this.state = new ApplicationState();
        this.outputSaver = new OutputSaver(OUTPUT_DIR, new DefaultChartCreator());
    }

    public TspApplication(ConsoleUI ui, DataManager dataManager,
                          AlgorithmSelector algorithmSelector,
                          TspRunner tspRunner,
                          ApplicationState state,
                          OutputSaver outputSaver) {
        this.ui = ui;
        this.dataManager = dataManager;
        this.algorithmSelector = algorithmSelector;
        this.tspRunner = tspRunner;
        this.state = state;
        this.outputSaver = outputSaver;
    }

    public void run() {
        ui.displayWelcome();
        Menu mainMenu = createMainMenu();

        while (!state.shouldExit()) {
            mainMenu.display();
            int choice = ui.getChoice("Choose option: ");

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
                .addItem("Save outputs",
                        this::saveOutputs)
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
            Tour tour = tspRunner.solve(state.getPoints(), state.getSelectedAlgorithm());
            state.setLastTour(tour);
            state.setLastAlgorithmName(state.getSelectedAlgorithm().getDisplayName());
            state.setLastSaved(false);
        } else {
            ui.showWarning("Please load or generate data first (option 1, 2 or 3)");
        }
    }

    private void saveOutputs() {
        if (state.getLastTour() == null) {
            ui.showWarning("No results to save. Run an algorithm first.");
            return;
        }

        if (state.isLastSaved()) {
            ui.showWarning("Outputs already saved for the last run.");
            return;
        }

        try {
            File dir = outputSaver.createRunDirectory(state.getLastAlgorithmName());
            outputSaver.saveChart(state.getLastTour(), dir, "chart.png");
            outputSaver.saveLogs(ui, dir);
            state.setLastSaved(true);
            ui.showSuccess("Outputs saved to: " + dir.getAbsolutePath());
        } catch (Exception e) {
            ui.showError("Failed to save outputs: " + e.getMessage());
        }
    }

    private void prepareForExit() {
        ui.showInfo("Goodbye!");
        state.exit();
    }
}
