package org.example;

import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.Tour;
import org.example.tsp.TspSolver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILE_NAME = "dane.txt";

    public static void main(String[] args) {
        PointGenerator generator = new PointGenerator();
        DataFileHandler fileHandler = new DataFileHandler(FILE_NAME);

        List<Point> points = generator.generate();

        try {
            fileHandler.writePoints(points);
        } catch (FileNotFoundException e) {
            System.err.println("Błąd zapisu: Nie można utworzyć pliku " + FILE_NAME);
            return;
        }

        List<Point> readPoints = fileHandler.readPoints();
        if (readPoints != null) {
            int numberOfPoints = readPoints.size();
            System.out.println("\nOdczytano dane z pliku " + FILE_NAME + ":");
            System.out.println("Liczba odczytanych punktów: " + numberOfPoints);

            for (int i = 0; i < numberOfPoints; i++) {
                Point point = readPoints.get(i);
                System.out.println((i + 1) + " " + point.getX() + " " + point.getY());
            }

            solveTsp(readPoints);
        }
    }

    private static List<City> convertPointsToCities(List<Point> points) {
        List<City> cities = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            String cityName = "City" + (i + 1);
            cities.add(new City(cityName, point.getX(), point.getY()));
        }
        return cities;
    }

    private static void solveTsp(List<Point> points) {
        List<City> cities = convertPointsToCities(points);

        TspSolver solver = new AntColonyTspSolver(points);
        //TspSolver solver = new GreedyTspSolver();
        Tour tour = solver.solve(cities);
        System.out.println(tour);
    } 
}
