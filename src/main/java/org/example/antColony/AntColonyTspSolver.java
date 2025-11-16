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

    // Macierz Odległości (D)
    private double[][] distanceMatrix;

    // Macierz Feromonów (T)
    private double[][] pheromoneMatrix;

    // Parametry algorytmu
    private static final double INITIAL_PHEROMONE = 1.0;
    private static final double PHEROMONE_EVAPORATION_COEFFICIENT = 0.1; //(RHO)
    private static final double PHEROMONE_WEIGHT = 1.0; //(Alpha)
    private static final double VISIBILITY_WEIGHT = 2.0; //(Beta)
    private static final double Q = 100.0; // Stała do obliczania ilości feromonu
    private static final int MAX_ITERATIONS = 100;

    private final int numberOfAnts;
    private final Random random;

    // Najlepsze rozwiązanie
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

    // matrix of distances between cities(points)
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

    //matrix of pheromone on each route
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
            // Lista tras dla wszystkich mrówek w tej iteracji
            List<Ant> ants = new ArrayList<>();

            // Konstrukcja tras dla każdej mrówki
            for (int antIndex = 0; antIndex < numberOfAnts; antIndex++) {
                Ant ant = new Ant();
                ant.constructTour();
                ants.add(ant);

                // Aktualizacja najlepszego rozwiązania
                if (ant.tourLength < bestTourLength) {
                    bestTourLength = ant.tourLength;
                    bestTour = new ArrayList<>(ant.tour);
                }
            }

            // Aktualizacja feromonów
            updatePheromones(ants);

            // Wyświetl postęp co 10 iteracji
            if ((iteration + 1) % 10 == 0) {
                System.out.printf("Iteracja %d: Najlepsza długość trasy = %.2f%n", iteration + 1, bestTourLength);
            }
        }

        System.out.printf("\n=== Algorytm zakończony ===\n");
        System.out.printf("Najlepsza długość trasy: %.2f%n", bestTourLength);

        // Konwersja najlepszej trasy na listę miast
        List<City> tourCities = new ArrayList<>();
        for (int cityIndex : bestTour) {
            tourCities.add(cities.get(cityIndex));
        }

        return new Tour(tourCities);
    }

    /**
     * Aktualizacja feromonów na wszystkich krawędziach
     */
    private void updatePheromones(List<Ant> ants) {
        // 1. Parowanie (ewaporacja) feromonów
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromoneMatrix[i][j] *= (1.0 - PHEROMONE_EVAPORATION_COEFFICIENT);
            }
        }

        // 2. Osadzanie nowych feromonów
        for (Ant ant : ants) {
            double pheromoneDeposit = Q / ant.tourLength;

            for (int i = 0; i < ant.tour.size(); i++) {
                int cityA = ant.tour.get(i);
                int cityB = ant.tour.get((i + 1) % ant.tour.size());

                pheromoneMatrix[cityA][cityB] += pheromoneDeposit;
                pheromoneMatrix[cityB][cityA] += pheromoneDeposit; // Graf nieskierowany
            }
        }
    }

    /**
     * Klasa reprezentująca pojedynczą mrówkę
     */
    private class Ant {
        List<Integer> tour;
        boolean[] visited;
        double tourLength;

        Ant() {
            tour = new ArrayList<>();
            visited = new boolean[numberOfCities];
            tourLength = 0.0;
        }

        /**
         * Konstruuje trasę dla mrówki
         */
        void constructTour() {
            // Losowy start
            int startCity = random.nextInt(numberOfCities);
            tour.add(startCity);
            visited[startCity] = true;

            // Budowanie trasy
            for (int step = 1; step < numberOfCities; step++) {
                int currentCity = tour.get(tour.size() - 1);
                int nextCity = selectNextCity(currentCity);

                tour.add(nextCity);
                visited[nextCity] = true;
                tourLength += distanceMatrix[currentCity][nextCity];
            }

            // Dodaj dystans powrotu do miasta startowego
            int lastCity = tour.get(tour.size() - 1);
            int firstCity = tour.get(0);
            tourLength += distanceMatrix[lastCity][firstCity];
        }

        /**
         * Wybiera następne miasto do odwiedzenia na podstawie prawdopodobieństwa
         */
        private int selectNextCity(int currentCity) {
            // Oblicz prawdopodobieństwa dla wszystkich nieodwiedzonych miast
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

            // Normalizuj prawdopodobieństwa
            for (int city = 0; city < numberOfCities; city++) {
                if (!visited[city]) {
                    probabilities[city] /= totalProbability;
                }
            }

            // Wybierz miasto metodą ruletki
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

            // Fallback: zwróć pierwsze nieodwiedzone miasto (nie powinno się zdarzyć)
            for (int city = 0; city < numberOfCities; city++) {
                if (!visited[city]) {
                    return city;
                }
            }

            return -1; // Nie powinno się nigdy zdarzyć
        }
    }
}