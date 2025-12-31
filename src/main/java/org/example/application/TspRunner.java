package org.example.application;

import org.example.domain.algorithm.Algorithm;
import org.example.domain.algorithm.AntColonyTspSolver;
import org.example.domain.algorithm.GreedyTspSolver;
import org.example.domain.algorithm.TspSolver;
import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;
import org.example.domain.service.CityConverter;
import org.example.ui.ConsoleUI;
import org.example.ui.TspVisualizer;

import java.util.List;

public class TspRunner {
    private final ConsoleUI ui;

    public TspRunner(ConsoleUI ui) {
        this.ui = ui;
    }

    public void solve(List<Point> points, Algorithm algorithm) {
        List<City> cities = CityConverter.fromPoints(points);

        TspSolver solver = createSolver(algorithm, points);
        String algorithmName = algorithm.getDisplayName();

        ui.displaySectionHeader("RUNNING: " + algorithmName);

        long startTime = System.currentTimeMillis();
        Tour tour = solver.solve(cities);
        long endTime = System.currentTimeMillis();

        ui.displaySectionHeader("FINAL RESULT - " + algorithmName);
        ui.println(tour.toString());
        ui.printf("Total execution time: %.3f seconds%n", (endTime - startTime) / 1000.0);
        ui.println("=".repeat(60));

        TspVisualizer.displayTour(tour, "TSP Visualization - " + algorithmName);

        ui.waitForEnter("\nPress Enter to return to menu...");
    }

    private TspSolver createSolver(Algorithm algorithm, List<Point> points) {
        return switch (algorithm) {
            case GREEDY -> new GreedyTspSolver();
            case ANT_COLONY -> new AntColonyTspSolver(points);
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };
    }
}

