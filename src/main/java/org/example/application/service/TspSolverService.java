package org.example.application.service;

import org.example.domain.algorithm.Algorithm;
import org.example.domain.algorithm.AntColonyTspSolver;
import org.example.domain.algorithm.GreedyTspSolver;
import org.example.domain.algorithm.TspSolver;
import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;
import org.example.domain.service.CityConverter;
import org.example.application.port.ConsoleOutput;
import org.example.application.port.MessagePresenter;
import org.example.ui.chart.ChartCreator;
import org.example.ui.chart.ChartDisplayer;
import org.jfree.chart.JFreeChart;

import java.util.List;

public class TspSolverService {
    private final ConsoleOutput output;
    private final MessagePresenter presenter;
    private final ChartCreator chartCreator;
    private final ChartDisplayer chartDisplayer;

    public TspSolverService(ConsoleOutput output, MessagePresenter presenter, ChartCreator chartCreator, ChartDisplayer chartDisplayer) {
        this.output = output;
        this.presenter = presenter;
        this.chartCreator = chartCreator;
        this.chartDisplayer = chartDisplayer;
    }

    public Tour solve(List<Point> points, Algorithm algorithm) {
        List<City> cities = CityConverter.fromPoints(points);

        TspSolver solver = createSolver(algorithm, points);
        String algorithmName = algorithm.getDisplayName();

        presenter.displaySectionHeader("RUNNING: " + algorithmName);

        long startTime = System.currentTimeMillis();
        Tour tour = solver.solve(cities);
        long endTime = System.currentTimeMillis();

        presenter.displaySectionHeader("FINAL RESULT - " + algorithmName);
        output.println(tour.toString());
        output.printf("Total execution time: %.3f seconds%n", (endTime - startTime) / 1000.0);
        output.println("=".repeat(60));

        JFreeChart chart = chartCreator.createChart(tour);
        chartDisplayer.display(chart, "TSP Visualization - " + algorithmName);

        return tour;
    }

    private TspSolver createSolver(Algorithm algorithm, List<Point> points) {
        return switch (algorithm) {
            case GREEDY -> new GreedyTspSolver();
            case ANT_COLONY -> new AntColonyTspSolver(points);
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };
    }
}

