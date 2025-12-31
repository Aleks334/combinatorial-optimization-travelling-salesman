package org.example.application;

import org.example.domain.model.Point;
import org.example.domain.service.PointGenerator;
import org.example.infrastructure.DataFileHandler;
import org.example.ui.ConsoleUI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final String fileName;
    private final ConsoleUI ui;

    public DataManager(String fileName, ConsoleUI ui) {
        this.fileName = fileName;
        this.ui = ui;
    }

    public List<Point> loadFromFile() {
        DataFileHandler fileHandler = new DataFileHandler(fileName);
        List<Point> points = fileHandler.readPoints();

        if (points != null && !points.isEmpty()) {
            ui.showSuccess("Loaded " + points.size() + " points from file " + fileName);
            displayPoints(points);
            return points;
        } else {
            ui.showError("Failed to load data from file " + fileName);
            return null;
        }
    }

    public List<Point> generate(int numberOfCities) {
        PointGenerator generator = new PointGenerator();
        List<Point> points;

        if (numberOfCities > 0) {
            points = generator.generate(numberOfCities);
            ui.showSuccess("Generated " + numberOfCities + " cities");
        } else {
            points = generator.generate();
            ui.showSuccess("Randomly generated " + points.size() + " cities");
        }

        displayPoints(points);
        return points;
    }

    public List<Point> generateAndSave(int numberOfCities) {
        List<Point> points = generate(numberOfCities);

        if (points != null && !points.isEmpty()) {
            DataFileHandler fileHandler = new DataFileHandler(fileName);
            try {
                fileHandler.writePoints(points);
                ui.showSuccess("Data saved to file " + fileName);
            } catch (FileNotFoundException e) {
                ui.showError("Write error: Cannot create file " + fileName);
            }
        }

        return points;
    }

    private void displayPoints(List<Point> points) {
        if (points == null || points.isEmpty()) return;

        int limit = Math.min(points.size(), 10);
        List<String> formattedCoordinates = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            Point point = points.get(i);
            formattedCoordinates.add(String.format("(%d, %d)", point.getX(), point.getY()));
        }

        ui.displayCoordinates(points.size(), formattedCoordinates);
    }
}

