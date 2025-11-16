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

    private static final double INITIAL_PHEROMONE = 1.0;
    private static final double PHEROMONE_EVAPORATION_COEFFICIENT = 0.1;
    private static final double PHEROMONE_WEIGHT = 1.0;
    private static final double VISIBILITY_WEIGHT = 2.0;
    private static final double Q = 100.0;
    private static final int MAX_ITERATIONS = 100;

    private final int numberOfAnts;
    private final Random random;

    private List<Integer> bestTour;
    private double bestTourLength;


    public AntColonyTspSolver(List<Point> points) {
        this.points = points;
        this.numberOfCities = points.size();
        this.numberOfAnts = this.numberOfCities;
        this.random = new Random();
        this.bestTourLength = Double.MAX_VALUE;

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
        System.out.println("\n=== Rozpoczęcie Algorytmu Mrówkowego ===");
        System.out.println("Liczba miast: " + numberOfCities);
        System.out.println("Liczba mrówek: " + numberOfAnts);
        System.out.println("Maksymalna liczba iteracji: " + MAX_ITERATIONS);
        System.out.println("Parametry: alfa=" + PHEROMONE_WEIGHT + ", beta=" + VISIBILITY_WEIGHT + ", rho=" + PHEROMONE_EVAPORATION_COEFFICIENT);

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            List<Ant> ants = new ArrayList<>();

            for (int antIndex = 0; antIndex < numberOfAnts; antIndex++) {
                Ant ant = new Ant();
                ant.constructTour();
                ants.add(ant);

                if (ant.tourLength < bestTourLength) {
                    bestTourLength = ant.tourLength;
                    bestTour = new ArrayList<>(ant.tour);
                }
            }

            updatePheromones(ants);

            if ((iteration + 1) % 10 == 0) {
                System.out.printf("Iteracja %d: Najlepsza długość trasy = %.2f%n", iteration + 1, bestTourLength);
            }
        }

        System.out.printf("\n=== Algorytm zakończony ===\n");
        System.out.printf("Najlepsza długość trasy: %.2f%n", bestTourLength);

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