package org.example.application.service;

import org.example.domain.algorithm.Algorithm;
import org.example.domain.algorithm.TspSolver;
import org.example.domain.algorithm.AntColonyTspSolver;
import org.example.domain.algorithm.GreedyTspSolver;

import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;
import org.example.domain.service.CityConverter;

import java.util.List;

public class TspSolverService {

    public Tour solve(List<Point> points, Algorithm algorithm) {
        List<City> cities = CityConverter.fromPoints(points);
        TspSolver solver = createSolver(algorithm, points);
        return solver.solve(cities);
    }

    private TspSolver createSolver(Algorithm algorithm, List<Point> points) {
        return switch (algorithm) {
            case GREEDY -> new GreedyTspSolver();
            case ANT_COLONY -> new AntColonyTspSolver(points);
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };
    }
}