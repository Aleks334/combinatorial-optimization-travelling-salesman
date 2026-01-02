package org.example.application;

import org.example.application.port.in.InputPort;
import org.example.application.port.out.OutputPort;
import org.example.application.port.out.SolutionArchiver;
import org.example.application.service.DataService;
import org.example.application.service.TspSolverService;
import org.example.domain.algorithm.Algorithm;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;
import org.example.ui.chart.ChartManager;
import org.example.ui.console.Menu;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TspApplication {

    private final InputPort input;
    private final OutputPort output;
    private final DataService dataService;
    private final TspSolverService solverService;
    private final SolutionArchiver archiver;
    private final ChartManager chartManager;

    private final SessionContext context;

    public TspApplication(InputPort input,
                          OutputPort output,
                          DataService dataService,
                          TspSolverService solverService,
                          SolutionArchiver archiver,
                          ChartManager chartManager) {
        this.input = input;
        this.output = output;
        this.dataService = dataService;
        this.solverService = solverService;
        this.archiver = archiver;
        this.chartManager = chartManager;
        this.context = new SessionContext();
    }

    public void run() {
        output.displayWelcome();
        Menu menu = createMenu();

        while (!context.shouldExit) {
            menu.display();
            int choice = input.getIntUntilValid("Choose option (1-7): ");
            try {
                menu.handleChoice(choice);
            } catch (Exception e) {
                output.displayError("Action failed: " + e.getMessage());
            }
        }
        System.exit(0);
    }

    private Menu createMenu() {
        return Menu.builder("Main Menu")
                .setOutput(output)
                .addItem("Load data from file", this::loadData)
                .addItem("Generate random data", this::generateData)
                .addItem("Generate and save data", this::generateAndSave)
                .addItem("Choose algorithm", this::chooseAlgorithm)
                .addItem("Solve TSP", this::solveTsp)
                .addItem("Save outputs", this::saveOutputs)
                .addItem("Exit", () -> context.shouldExit = true)
                .build();
    }

    private void loadData() {
        try {
            List<Point> points = dataService.loadFromFile();
            if (points.isEmpty()) {
                output.displayError("File is empty.");
            } else {
                updatePoints(points);
                output.displaySuccess("Loaded " + points.size() + " points.");
            }
        } catch (Exception e) {
            output.displayError("Load failed: " + e.getMessage());
        }
    }

    private void generateData() {
        int count = input.getIntUntilValid("Enter number of cities (0 for random): ");
        List<Point> points = dataService.generateRandom(count);
        updatePoints(points);
        output.displaySuccess("Generated " + points.size() + " cities.");
    }

    private void generateAndSave() {
        int count = input.getIntUntilValid("Enter number of cities (0 for random): ");
        List<Point> points = dataService.generateRandom(count);
        try {
            dataService.saveToFile(points);
            updatePoints(points);
            output.displaySuccess("Generated and saved " + points.size() + " cities.");
        } catch (Exception e) {
            output.displayError("Save failed: " + e.getMessage());
        }
    }

    private void chooseAlgorithm() {
        output.displayHeader("Choose Algorithm");
        output.println("  1. Greedy Algorithm");
        output.println("  2. Ant Colony Optimization");
        int choice = input.getIntUntilValid("Select (1-2): ");

        context.selectedAlgorithm = (choice == 1) ? Algorithm.GREEDY : Algorithm.ANT_COLONY;
        output.displaySuccess("Selected: " + context.selectedAlgorithm.getDisplayName());
    }

    private void solveTsp() {
        if (context.points == null || context.points.isEmpty()) {
            output.displayError("No points loaded. Load or generate data first.");
            return;
        }

        output.displayHeader("Running " + context.selectedAlgorithm.getDisplayName());

        long start = System.currentTimeMillis();
        Tour tour = solverService.solve(context.points, context.selectedAlgorithm);
        long duration = System.currentTimeMillis() - start;

        context.lastTour = tour;
        context.lastAlgorithmName = context.selectedAlgorithm.getDisplayName();
        context.isResultSaved = false;

        output.displayHeader("Result");
        output.println(tour.toString());
        output.printf("Time: %.3f s%n", duration / 1000.0);

        JFreeChart chart = chartManager.createChart(tour);
        chartManager.display(chart, "TSP - " + context.lastAlgorithmName);
    }

    private void saveOutputs() {
        if (context.lastTour == null) {
            output.displayError("No solution to save. Run solver first.");
            return;
        }
        if (context.isResultSaved) {
            output.displayError("Already saved.");
            return;
        }

        try {
            File dir = archiver.createRunDirectory(context.lastAlgorithmName);
            JFreeChart chart = chartManager.createChart(context.lastTour);

            archiver.saveLogs(output.getLogs(), dir);
            archiver.saveChart(chart, dir);

            context.isResultSaved = true;
            output.displaySuccess("Saved to: " + dir.getAbsolutePath());
        } catch (Exception e) {
            output.displayError("Failed to save: " + e.getMessage());
        }
    }

    private void updatePoints(List<Point> points) {
        context.points = points;
        context.lastTour = null;
        context.isResultSaved = false;

        int limit = Math.min(points.size(), 10);
        List<String> preview = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            Point p = points.get(i);
            preview.add(String.format("(%d, %d)", p.getX(), p.getY()));
        }

        if (points.size() > limit) preview.add("... " + (points.size() - limit) + " more");
        output.displayPoints(preview);
    }

    private static class SessionContext {
        List<Point> points;
        Algorithm selectedAlgorithm = Algorithm.ANT_COLONY;
        Tour lastTour;
        String lastAlgorithmName;
        boolean isResultSaved;
        boolean shouldExit = false;
    }
}