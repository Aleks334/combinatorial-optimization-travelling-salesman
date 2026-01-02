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

    private static final double MIN_PHEROMONE = 0.01;
    private static final double MAX_PHEROMONE = 10.0;

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

    private final int maxIterations;

    private final int numberOfAnts;
    private final Random random;

    private List<Integer> bestTour;
    private double bestTourLength;

    private int iterationsCompleted;

    private double adaptivePheromoneWeight;
    private double adaptiveVisibilityWeight;
    private int stagnationCounter = 0;


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

        this.adaptivePheromoneWeight = PHEROMONE_WEIGHT;
        this.adaptiveVisibilityWeight = VISIBILITY_WEIGHT;

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

            IterationOutcome outcome = runIteration();

            updatePheromones(outcome.ants);

            if (!outcome.foundImprovement) {
                iterationsWithoutImprovement++;
                stagnationCounter++;
                adaptParameters();

                if (iterationsWithoutImprovement >= STAGNATION_LIMIT) {
                    this.iterationsCompleted = iteration + 1;
                    break;
                }
            } else {
                iterationsWithoutImprovement = 0;
                stagnationCounter = Math.max(0, stagnationCounter - 2);
            }

            this.iterationsCompleted = iteration + 1;
        }

        List<City> tourCities = buildTourFromBest(cities);
        return new Tour(tourCities);
    }

    private IterationOutcome runIteration() {
        List<Ant> ants = new ArrayList<>();
        boolean foundImprovement = false;

        for (int antIndex = 0; antIndex < numberOfAnts; antIndex++) {
            Ant ant = new Ant(numberOfCities, distanceMatrix, pheromoneMatrix, random,
                    adaptivePheromoneWeight, adaptiveVisibilityWeight);
            ant.constructTour();

            improveWithLocalSearch(ant.tour);
            recalculateTourLength(ant);

            ants.add(ant);

            if (ant.tourLength < bestTourLength) {
                bestTourLength = ant.tourLength;
                bestTour = new ArrayList<>(ant.tour);
                foundImprovement = true;
            }
        }

        return new IterationOutcome(ants, foundImprovement);
    }

    private void recalculateTourLength(Ant ant) {
        ant.tourLength = 0.0;
        for (int i = 0; i < ant.tour.size(); i++) {
            int cityA = ant.tour.get(i);
            int cityB = ant.tour.get((i + 1) % ant.tour.size());
            ant.tourLength += distanceMatrix[cityA][cityB];
        }
    }

    private List<City> buildTourFromBest(List<City> cities) {
        List<City> tourCities = new ArrayList<>();
        if (bestTour == null || bestTour.isEmpty()) {
            return tourCities;
        }
        for (int cityIndex : bestTour) {
            tourCities.add(cities.get(cityIndex));
        }
        return tourCities;
    }

    private static class IterationOutcome {
        final List<Ant> ants;
        final boolean foundImprovement;

        IterationOutcome(List<Ant> ants, boolean foundImprovement) {
            this.ants = ants;
            this.foundImprovement = foundImprovement;
        }
    }

    private void updatePheromones(List<Ant> ants) {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromoneMatrix[i][j] *= (1.0 - PHEROMONE_EVAPORATION_COEFFICIENT);
            }
        }

        depositPheromones(ants);
        reinforceBestTour();
        applyMinMaxBounds();
    }

    private void depositPheromones(List<Ant> ants) {
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

    private void reinforceBestTour() {
        if (bestTour != null && !bestTour.isEmpty()) {
            double bestTourPheromoneDeposit = Q / bestTourLength;

            for (int i = 0; i < bestTour.size(); i++) {
                int cityA = bestTour.get(i);
                int cityB = bestTour.get((i + 1) % bestTour.size());

                pheromoneMatrix[cityA][cityB] += bestTourPheromoneDeposit;
                pheromoneMatrix[cityB][cityA] += bestTourPheromoneDeposit;
            }
        }
    }

    private void applyMinMaxBounds() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                if (pheromoneMatrix[i][j] < MIN_PHEROMONE) {
                    pheromoneMatrix[i][j] = MIN_PHEROMONE;
                } else if (pheromoneMatrix[i][j] > MAX_PHEROMONE) {
                    pheromoneMatrix[i][j] = MAX_PHEROMONE;
                }
            }
        }
    }

    private double calculate2OptGain(List<Integer> tour, int i, int j) {
        int n = tour.size();
        int a = tour.get(i);
        int b = tour.get((i + 1) % n);
        int c = tour.get(j);
        int d = tour.get((j + 1) % n);

        double currentDistance = distanceMatrix[a][b] + distanceMatrix[c][d];
        double newDistance = distanceMatrix[a][c] + distanceMatrix[b][d];

        return currentDistance - newDistance;
    }

    private void apply2Opt(List<Integer> tour, int i, int j) {
        int start = i + 1;
        int end = j;

        while (start < end) {
            int temp = tour.get(start);
            tour.set(start, tour.get(end));
            tour.set(end, temp);
            start++;
            end--;
        }
    }

    private void improveWithLocalSearch(List<Integer> tour) {
        boolean improved = true;

        while (improved) {
            improved = false;
            int n = tour.size();

            for (int i = 0; i < n - 2; i++) {
                for (int j = i + 2; j < n; j++) {
                    double gain = calculate2OptGain(tour, i, j);

                    if (gain > 0.01) {
                        apply2Opt(tour, i, j);
                        improved = true;
                        break;
                    }
                }
                if (improved) break;
            }
        }
    }

    private void adaptParameters() {
        final int STAGNATION_THRESHOLD_FOR_ADAPT = 10;
        final double VISIBILITY_INCREMENT_ON_STAGNATION = 0.5;
        final double PHEROMONE_DECREMENT_ON_STAGNATION = 0.2;
        final double VISIBILITY_MAX = 10.0;
        final double PHEROMONE_MIN = 0.5;

        final double VISIBILITY_DECREMENT = 0.1;
        final double VISIBILITY_MIN = 1.0;
        final double PHEROMONE_INCREMENT = 0.1;
        final double PHEROMONE_MAX = 2.0;

        if (stagnationCounter > STAGNATION_THRESHOLD_FOR_ADAPT) {
            adaptiveVisibilityWeight = Math.min(adaptiveVisibilityWeight + VISIBILITY_INCREMENT_ON_STAGNATION, VISIBILITY_MAX);
            adaptivePheromoneWeight = Math.max(adaptivePheromoneWeight - PHEROMONE_DECREMENT_ON_STAGNATION, PHEROMONE_MIN);
        } else {
            adaptiveVisibilityWeight = Math.max(adaptiveVisibilityWeight - VISIBILITY_DECREMENT, VISIBILITY_MIN);
            adaptivePheromoneWeight = Math.min(adaptivePheromoneWeight + PHEROMONE_INCREMENT, PHEROMONE_MAX);
        }
    }
}