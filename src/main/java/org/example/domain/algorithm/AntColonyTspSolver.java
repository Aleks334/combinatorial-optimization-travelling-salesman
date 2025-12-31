package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class AntColonyTspSolver implements TspSolver {

    private final List<Point> points;
    private final int numberOfCities;

    private double[][] distanceMatrix;
    private double[][] pheromoneMatrix;

    private static final double INITIAL_PHEROMONE = 0.1;
    private static final double PHEROMONE_EVAPORATION_COEFFICIENT = 0.5;
    private static final double PHEROMONE_WEIGHT = 1.0;
    private static final double VISIBILITY_WEIGHT = 5.0;
    private static final double Q = 100.0;

    private static final int MIN_ANTS = 20;
    private static final int STAGNATION_LIMIT = 50;
    private static final long MAX_TIME_MS = 180000;

    private static final int SMALL_CITY_THRESHOLD = 20;
    private static final int MEDIUM_CITY_THRESHOLD = 50;
    private static final int LARGE_CITY_THRESHOLD = 100;

    private static final int SMALL_CITY_MAX_ITERATIONS = 500;
    private static final int MEDIUM_CITY_MAX_ITERATIONS = 300;
    private static final int LARGE_CITY_MAX_ITERATIONS = 200;
    private static final int VERY_LARGE_CITY_MAX_ITERATIONS = 100;

    private static final int PROGRESS_LOG_INTERVAL = 50;

    private final int maxIterations;

    private final int numberOfAnts;
    private final Random random;

    private List<Integer> bestTour;
    private double bestTourLength;

    private int iterationsCompleted;


    public AntColonyTspSolver(List<Point> points) {
        if (points == null) {
            throw new IllegalArgumentException("Points list cannot be null");
        }
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Points list cannot be empty");
        }

        this.points = points;
        this.numberOfCities = points.size();

        this.numberOfAnts = Math.max(this.numberOfCities, MIN_ANTS);

        if (numberOfCities <= SMALL_CITY_THRESHOLD) {
            this.maxIterations = SMALL_CITY_MAX_ITERATIONS;
        } else if (numberOfCities <= MEDIUM_CITY_THRESHOLD) {
            this.maxIterations = MEDIUM_CITY_MAX_ITERATIONS;
        } else if (numberOfCities <= LARGE_CITY_THRESHOLD) {
            this.maxIterations = LARGE_CITY_MAX_ITERATIONS;
        } else {
            this.maxIterations = VERY_LARGE_CITY_MAX_ITERATIONS;
        }

        this.random = new Random();
        this.bestTourLength = Double.MAX_VALUE;
        this.iterationsCompleted = 0;

        initializeDistanceMatrix();
        initializePheromoneMatrix();
    }

    private void initializeDistanceMatrix() {
        this.distanceMatrix = new double[numberOfCities][numberOfCities];

        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0.0;
                } else {
                    distanceMatrix[i][j] = points.get(i).distanceTo(points.get(j));
                }
            }
        }
    }

    private void initializePheromoneMatrix() {
        this.pheromoneMatrix = new double[numberOfCities][numberOfCities];

        for (double[] row : pheromoneMatrix) {
            Arrays.fill(row, INITIAL_PHEROMONE);
        }
    }


    @Override
    public Tour solve(List<City> cities) {
        int iterationsWithoutImprovement = 0;
        long startTime = System.currentTimeMillis();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            if (System.currentTimeMillis() - startTime > MAX_TIME_MS) {
                break;
            }

            List<Ant> ants = new ArrayList<>();
            boolean foundImprovement = false;

            for (int antIndex = 0; antIndex < numberOfAnts; antIndex++) {
                Ant ant = new Ant(numberOfCities, distanceMatrix, pheromoneMatrix, random,
                                  PHEROMONE_WEIGHT, VISIBILITY_WEIGHT);
                ant.constructTour();
                ants.add(ant);

                if (ant.tourLength < bestTourLength) {
                    bestTourLength = ant.tourLength;
                    bestTour = new ArrayList<>(ant.tour);
                    foundImprovement = true;
                    iterationsWithoutImprovement = 0;
                }
            }

            updatePheromones(ants);

            if (!foundImprovement) {
                iterationsWithoutImprovement++;

                if (iterationsWithoutImprovement >= STAGNATION_LIMIT) {
                    this.iterationsCompleted = iteration + 1;
                    break;
                }
            }

            this.iterationsCompleted = iteration + 1;
        }

        List<City> tourCities = new ArrayList<>();
        for (int cityIndex : bestTour) {
            tourCities.add(cities.get(cityIndex));
        }

        return new Tour(tourCities);
    }

    private void updatePheromones(List<Ant> ants) {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromoneMatrix[i][j] *= (1.0 - PHEROMONE_EVAPORATION_COEFFICIENT);
            }
        }

        for (Ant ant : ants) {
            double pheromoneDeposit = Q / ant.tourLength;

            for (int i = 0; i < ant.tour.size(); i++) {
                int cityA = ant.tour.get(i);
                int cityB = ant.tour.get((i + 1) % ant.tour.size());

                pheromoneMatrix[cityA][cityB] += pheromoneDeposit;
                pheromoneMatrix[cityB][cityA] += pheromoneDeposit;
            }
        }
    }

    public int getIterationsCompleted() {
        return iterationsCompleted;
    }

    public double getBestTourLength() {
        return bestTourLength;
    }
}