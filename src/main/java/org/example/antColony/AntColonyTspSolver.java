package org.example.antColony;

import org.example.Point;
import org.example.tsp.City;
import org.example.tsp.Tour;
import org.example.tsp.TspSolver;

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

    private final int maxIterations;

    private static final int STAGNATION_LIMIT = 50;
    private static final long MAX_TIME_MS = 180000;

    private final int numberOfAnts;
    private final Random random;

    private List<Integer> bestTour;
    private double bestTourLength;

    private long executionTimeMs;
    private int iterationsCompleted;


    public AntColonyTspSolver(List<Point> points) {
        this.points = points;
        this.numberOfCities = points.size();

        this.numberOfAnts = Math.max(this.numberOfCities, 20);

        if (numberOfCities <= 20) {
            this.maxIterations = 500;
        } else if (numberOfCities <= 50) {
            this.maxIterations = 300;
        } else if (numberOfCities <= 100) {
            this.maxIterations = 200;
        } else {
            this.maxIterations = 100;
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
        System.out.println("Zainicjowano Macierz Odległości.");
    }

    private void initializePheromoneMatrix() {
        this.pheromoneMatrix = new double[numberOfCities][numberOfCities];

        for (double[] row : pheromoneMatrix) {
            Arrays.fill(row, INITIAL_PHEROMONE);
        }
        System.out.println("Zainicjowano Macierz Feromonów początkową wartością: " + INITIAL_PHEROMONE);
    }


    @Override
    public Tour solve(List<City> cities) {
        long startTime = System.currentTimeMillis();

        System.out.println("\n=== Rozpoczęcie Algorytmu Mrówkowego (ACO) ===");
        System.out.println("Liczba miast: " + numberOfCities);
        System.out.println("Liczba mrówek: " + numberOfAnts);
        System.out.println("Maksymalna liczba iteracji: " + maxIterations);
        System.out.println("Limit stagnacji: " + STAGNATION_LIMIT + " iteracji");
        System.out.println("Maksymalny czas: " + (MAX_TIME_MS / 1000) + " sekund");
        System.out.println("Parametry: alfa=" + PHEROMONE_WEIGHT + ", beta=" + VISIBILITY_WEIGHT + ", rho=" + PHEROMONE_EVAPORATION_COEFFICIENT);

        int iterationsWithoutImprovement = 0;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > MAX_TIME_MS) {
                System.out.println("\n*** Osiągnięto limit czasu - przerywanie algorytmu ***");
                break;
            }

            List<Ant> ants = new ArrayList<>();
            boolean foundImprovement = false;

            for (int antIndex = 0; antIndex < numberOfAnts; antIndex++) {
                Ant ant = new Ant();
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
                    System.out.println("\n*** Osiągnięto limit stagnacji (" + STAGNATION_LIMIT + " iteracji bez poprawy) - przerywanie algorytmu ***");
                    this.iterationsCompleted = iteration + 1;
                    break;
                }
            }

            if ((iteration + 1) % 10 == 0 || foundImprovement) {
                long elapsedTime = (currentTime - startTime) / 1000;
                System.out.printf("Iteracja %d/%d: Najlepsza długość = %.2f (Czas: %ds, Bez poprawy: %d)%n",
                    iteration + 1, maxIterations, bestTourLength, elapsedTime, iterationsWithoutImprovement);
            }

            this.iterationsCompleted = iteration + 1;
        }

        long endTime = System.currentTimeMillis();
        this.executionTimeMs = endTime - startTime;

        System.out.printf("\n=== Algorytm zakończony ===\n");
        System.out.printf("Liczba wykonanych iteracji: %d/%d%n", iterationsCompleted, maxIterations);
        System.out.printf("Najlepsza długość trasy: %.2f%n", bestTourLength);
        System.out.printf("Czas wykonania: %.3f sekund (%.0f ms)%n", executionTimeMs / 1000.0, (double) executionTimeMs);

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

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public int getIterationsCompleted() {
        return iterationsCompleted;
    }

    public double getBestTourLength() {
        return bestTourLength;
    }

    private class Ant {
        List<Integer> tour;
        boolean[] visited;
        double tourLength;

        Ant() {
            tour = new ArrayList<>();
            visited = new boolean[numberOfCities];
            tourLength = 0.0;
        }

        void constructTour() {
            int startCity = random.nextInt(numberOfCities);
            tour.add(startCity);
            visited[startCity] = true;

            for (int step = 1; step < numberOfCities; step++) {
                int currentCity = tour.get(tour.size() - 1);
                int nextCity = selectNextCity(currentCity);

                tour.add(nextCity);
                visited[nextCity] = true;
                tourLength += distanceMatrix[currentCity][nextCity];
            }

            int lastCity = tour.get(tour.size() - 1);
            int firstCity = tour.get(0);
            tourLength += distanceMatrix[lastCity][firstCity];
        }

        private int selectNextCity(int currentCity) {
            double[] probabilities = new double[numberOfCities];
            double totalProbability = 0.0;

            for (int city = 0; city < numberOfCities; city++) {
                if (!visited[city]) {
                    double pheromone = Math.pow(pheromoneMatrix[currentCity][city], PHEROMONE_WEIGHT);
                    double visibility = Math.pow(1.0 / distanceMatrix[currentCity][city], VISIBILITY_WEIGHT);
                    probabilities[city] = pheromone * visibility;
                    totalProbability += probabilities[city];
                }
            }

            for (int city = 0; city < numberOfCities; city++) {
                if (!visited[city]) {
                    probabilities[city] /= totalProbability;
                }
            }

            double randomValue = random.nextDouble();
            double cumulativeProbability = 0.0;

            for (int city = 0; city < numberOfCities; city++) {
                if (!visited[city]) {
                    cumulativeProbability += probabilities[city];
                    if (randomValue <= cumulativeProbability) {
                        return city;
                    }
                }
            }

            // fallback
            for (int city = 0; city < numberOfCities; city++) {
                if (!visited[city]) {
                    return city;
                }
            }

            return -1;
        }
    }
}