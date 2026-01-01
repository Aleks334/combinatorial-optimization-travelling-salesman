package org.example.infrastructure.file;

import org.example.domain.model.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class DataFileHandler {
    private final String fileName;

    public DataFileHandler(String fileName) {
        this.fileName = Objects.requireNonNull(fileName, "File name cannot be null");
    }

    public void writePoints(List<Point> points) throws DataSaveException {
        Objects.requireNonNull(points, "Points list cannot be null");
        if (points.isEmpty()) {
            throw new DataSaveException("Cannot save empty points list");
        }

        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(points.size());

            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                writer.println((i + 1) + " " + point.getX() + " " + point.getY());
            }
        } catch (FileNotFoundException e) {
            throw new DataSaveException("Cannot create file: " + fileName, e);
        }
    }

    public List<Point> readPoints() throws DataReadException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new DataReadException("File not found: " + fileName);
        }

        try (Scanner scanner = new Scanner(new File(fileName))) {
            if (!scanner.hasNextInt()) {
                throw new DataReadException(
                        "Invalid format: first line must contain point count"
                );
            }

            int numberOfPoints = scanner.nextInt();
            scanner.nextLine();

            List<Point> points = new ArrayList<>();

            for (int i = 0; i < numberOfPoints; i++) {
                try {
                    if (!scanner.hasNextInt()) {
                        throw new DataReadException(
                                String.format("Missing index for point %d", i + 1)
                        );
                    }
                    scanner.nextInt();

                    int x = readCoordinate(scanner, "X", i + 1);
                    int y = readCoordinate(scanner, "Y", i + 1);

                    points.add(new Point(x, y));
                } catch (NumberFormatException e) {
                    throw new DataReadException(
                            String.format("Invalid coordinate format for point %d", i + 1), e
                    );
                }
            }
            return points;

        } catch (FileNotFoundException e) {
            throw new DataReadException("File access error: " + fileName, e);
        }
    }

    private int readCoordinate(Scanner scanner, String type, int pointIndex)
            throws DataReadException {
        if (!scanner.hasNextInt()) {
            throw new DataReadException(
                    String.format("Missing %s coordinate for point %d", type, pointIndex)
            );
        }
        return scanner.nextInt();
    }
}
