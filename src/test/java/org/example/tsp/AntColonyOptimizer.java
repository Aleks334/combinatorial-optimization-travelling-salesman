package org.example.tsp;

import org.example.Point;
import java.util.List;
import java.util.Arrays;


public class AntColonyOptimizer {

    private final List<Point> points;
    private final int numberOfCities;

    // Macierz Odległości (D)
    private double[][] distanceMatrix;

    // Macierz Feromonów (T)
    private double[][] pheromoneMatrix;


    private static final double INITIAL_PHEROMONE = 1.0;
    private static final double PHEROMONE_EVAPORATION_COEFFICIENT = 0.1; //(RHO)
    private static final double PHEROMONE_WEIGHT = 1.0; //(Alpha)
    private static final double VISIBILITY_WEIGHT = 2.0; //(Beta)

    private final int numberOfAnts;


    public AntColonyOptimizer(List<Point> points) {
        this.points = points;
        this.numberOfCities = points.size();

        this.numberOfAnts = this.numberOfCities;


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
                    //  Point.distanceTo()
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



}