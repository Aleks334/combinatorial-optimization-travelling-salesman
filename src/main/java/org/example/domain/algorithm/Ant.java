package org.example.domain.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Ant {
    private final int numberOfCities;
    private final double[][] distanceMatrix;
    private final double[][] pheromoneMatrix;
    private final Random random;
    private final double pheromoneWeight;
    private final double visibilityWeight;

    List<Integer> tour;
    boolean[] visited;
    double tourLength;

    Ant(int numberOfCities, double[][] distanceMatrix, double[][] pheromoneMatrix,
        Random random, double pheromoneWeight, double visibilityWeight) {
        this.numberOfCities = numberOfCities;
        this.distanceMatrix = distanceMatrix;
        this.pheromoneMatrix = pheromoneMatrix;
        this.random = random;
        this.pheromoneWeight = pheromoneWeight;
        this.visibilityWeight = visibilityWeight;

        this.tour = new ArrayList<>();
        this.visited = new boolean[numberOfCities];
        this.tourLength = 0.0;
    }

    void constructTour() {
        int startCity = random.nextInt(numberOfCities);
        tour.add(startCity);
        visited[startCity] = true;

        for (int step = 1; step < numberOfCities; step++) {
            int currentCity = tour.getLast();
            int nextCity = selectNextCity(currentCity);

            tour.add(nextCity);
            visited[nextCity] = true;
            tourLength += distanceMatrix[currentCity][nextCity];
        }

        int lastCity = tour.getLast();
        int firstCity = tour.getFirst();
        tourLength += distanceMatrix[lastCity][firstCity];
    }

    private int selectNextCity(int currentCity) {
        double[] probabilities = new double[numberOfCities];
        double totalProbability = 0.0;

        for (int city = 0; city < numberOfCities; city++) {
            if (!visited[city]) {
                double pheromone = Math.pow(pheromoneMatrix[currentCity][city], pheromoneWeight);
                double visibility = Math.pow(1.0 / distanceMatrix[currentCity][city], visibilityWeight);
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

        for (int city = 0; city < numberOfCities; city++) {
            if (!visited[city]) {
                return city;
            }
        }

        return -1;
    }
}

