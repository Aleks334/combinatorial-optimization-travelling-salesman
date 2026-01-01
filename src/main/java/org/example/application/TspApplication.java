package org.example.application;

import org.example.application.port.output.OutputSaverPort;
import org.example.application.service.DataService;
import org.example.application.service.AlgorithmService;
import org.example.application.service.TspSolverService;
import org.example.ui.console.ConsoleUIFacade;
import org.example.ui.console.Menu;
import org.example.ui.chart.DefaultChartCreator;
import org.example.ui.chart.DefaultChartDisplayer;
import org.example.infrastructure.output.FileOutputSaver;
import org.example.infrastructure.file.DataFileHandler;
import org.example.domain.model.Tour;
import org.example.domain.service.PointGenerator;
import org.jfree.chart.JFreeChart;

import java.io.File;

public class TspApplication implements Application {
    private final String FILE_NAME = "data.txt";
    private final String OUTPUT_DIR = "outputs";

    private final ConsoleUIFacade ui;
    private final DataService dataService;
    private final AlgorithmService algorithmService;
    private final TspSolverService tspSolverService;

    private final ApplicationState state;
    private final OutputSaverPort outputSaverPort;
    private final DefaultChartCreator chartCreator;

    public TspApplication() {
        this.ui = new ConsoleUIFacade();

        DataFileHandler fileHandler = new DataFileHandler(FILE_NAME);
        PointGenerator generator = new PointGenerator();
        this.dataService = new DataService(fileHandler, generator, ui.presenter());
        this.algorithmService = new AlgorithmService(ui.output(), ui.input(), ui.presenter());
        this.tspSolverService = new TspSolverService(ui.output(), ui.presenter(), new DefaultChartCreator(), new DefaultChartDisplayer());

        this.state = new ApplicationState();
        this.outputSaverPort = new FileOutputSaver(OUTPUT_DIR);
        this.chartCreator = new DefaultChartCreator();
    }

    public TspApplication(ConsoleUIFacade ui, DataService dataService,
                          AlgorithmService algorithmService,
                          TspSolverService tspSolverService,
                          ApplicationState state,
                          OutputSaverPort outputSaverPort) {
        this.ui = ui;
        this.dataService = dataService;
        this.algorithmService = algorithmService;
        this.tspSolverService = tspSolverService;
        this.state = state;
        this.outputSaverPort = outputSaverPort;
        this.chartCreator = new DefaultChartCreator();
    }

    public void run() {
        ui.presenter().displayWelcome();
        Menu mainMenu = createMainMenu();

        while (!state.shouldExit()) {
            mainMenu.display();
            int choice = ui.input().getIntUntilValid("Choose option (1-7): ");

            if (choice == -1) {
                ui.presenter().showWarning("Invalid input. Please enter a number.");
                continue;
            }

            try {
                mainMenu.handleChoice(choice);
            } catch (Exception e) {
                ui.presenter().showWarning("Invalid choice. Please try again.");
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
        state.setPoints(dataService.loadFromFile());
    }

    private void generateRandomData() {
        int count = ui.input().getIntUntilValid("\nEnter number of cities to generate (or 0 for random 1-50): ");
        state.setPoints(dataService.generate(count));
    }

    private void generateAndSaveData() {
        int count = ui.input().getIntUntilValid("\nEnter number of cities to generate (or 0 for random 1-50): ");
        state.setPoints(dataService.generateAndSave(count));
    }

    private void chooseAlgorithm() {
        state.setSelectedAlgorithm(algorithmService.selectAlgorithm());
    }

    private void solveTsp() {
        if (state.hasPoints()) {
            Tour tour = tspSolverService.solve(state.getPoints(), state.getSelectedAlgorithm());
            state.setLastTour(tour);
            state.setLastAlgorithmName(state.getSelectedAlgorithm().getDisplayName());
            state.setLastSaved(false);
        } else {
            ui.presenter().showWarning("Please load or generate data first (option 1, 2 or 3)");
        }
    }

    private void saveOutputs() {
        if (state.getLastTour() == null) {
            ui.presenter().showWarning("No results to save. Run an algorithm first.");
            return;
        }

        if (state.isLastSaved()) {
            ui.presenter().showWarning("Outputs already saved for the last run.");
            return;
        }

        try {
            File dir = outputSaverPort.createRunDirectory(state.getLastAlgorithmName());
            JFreeChart chart = chartCreator.createChart(state.getLastTour());
            outputSaverPort.saveChart(chart, dir, "chart.png");
            outputSaverPort.saveLogs(ui.logManager(), dir);
            state.setLastSaved(true);
            ui.presenter().showSuccess("Outputs saved to: " + dir.getAbsolutePath());
        } catch (Exception e) {
            ui.presenter().showError("Failed to save outputs: " + e.getMessage());
        }
    }

    private void prepareForExit() {
        ui.presenter().showInfo("Goodbye!");
        state.exit();
    }
}

