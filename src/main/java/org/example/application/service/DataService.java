package org.example.application.service;

import org.example.domain.model.Point;
import org.example.domain.service.PointGenerator;
import org.example.infrastructure.file.DataFileHandler;
import org.example.infrastructure.file.DataReadException;
import org.example.infrastructure.file.DataSaveException;
import org.example.application.port.MessagePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DataService {
    private final DataFileHandler fileHandler;
    private final PointGenerator generator;
    private final MessagePresenter presenter;

    public DataService(DataFileHandler fileHandler, PointGenerator generator, MessagePresenter presenter) {
        this.fileHandler = Objects.requireNonNull(fileHandler, "DataFileHandler cannot be null");
        this.generator = Objects.requireNonNull(generator, "PointGenerator cannot be null");
        this.presenter = Objects.requireNonNull(presenter, "MessagePresenter cannot be null");
    }

    public List<Point> loadFromFile() {
        try {
            List<Point> points = fileHandler.readPoints();

            if (points.isEmpty()) {
                presenter.showWarning("File is empty");
                return Collections.emptyList();
            }

            presenter.showSuccess("Loaded " + points.size() + " points from file");
            displayPoints(points);
            return points;

        } catch (DataReadException e) {
            presenter.showError("Failed to load data: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Point> generate(int numberOfCities) {
        List<Point> points;

        if (numberOfCities > 0) {
            points = generator.generate(numberOfCities);
            presenter.showSuccess("Generated " + numberOfCities + " cities");
        } else {
            points = generator.generate();
            presenter.showSuccess("Randomly generated " + points.size() + " cities");
        }

        displayPoints(points);
        return points;
    }

    public List<Point> generateAndSave(int numberOfCities) {
        List<Point> points = generate(numberOfCities);

        if (!points.isEmpty()) {
            try {
                fileHandler.writePoints(points);
                presenter.showSuccess("Data saved to file");
            } catch (DataSaveException e) {
                presenter.showError("Save error: " + e.getMessage());
            }
        }

        return points;
    }

    private void displayPoints(List<Point> points) {
        Objects.requireNonNull(points);

        if (points.isEmpty()) {
            presenter.showInfo("No points to display");
            return;
        }

        int limit = Math.min(points.size(), 10);
        List<String> formattedCoordinates = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            Point point = points.get(i);
            formattedCoordinates.add(String.format("(%d, %d)", point.getX(), point.getY()));
        }

        if (points.size() > limit) {
            formattedCoordinates.add(String.format("... and %d more", points.size() - limit));
        }

        presenter.displayCoordinates(points.size(), formattedCoordinates);
    }
}

