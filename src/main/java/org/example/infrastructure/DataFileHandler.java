package org.example.infrastructure;

import org.example.domain.model.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataFileHandler {
    private final String fileName;

    public DataFileHandler(String fileName) {
        this.fileName = fileName;
    }

    public void writePoints(List<Point> points) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(points.size());

            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                writer.println((i + 1) + " " + point.getX() + " " + point.getY());
            }
        }
    }

    public List<Point> readPoints() {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            if (!scanner.hasNextInt()) {
                System.err.println("Read error: File does not contain the number of points in the first line.");
                return null;
            }

            int numberOfPoints = scanner.nextInt();
            scanner.nextLine();

            List<Point> points = new ArrayList<>();

            for (int i = 0; i < numberOfPoints; i++) {
                if (scanner.hasNextInt()) {
                    scanner.nextInt();
                } else {
                    System.err.println("Read error: Missing index for point " + (i + 1));
                    return null;
                }

                if (!scanner.hasNextInt()) {
                    System.err.println("Read error: Missing X coordinate data for point " + (i + 1));
                    return null;
                }
                int x = scanner.nextInt();

                if (!scanner.hasNextInt()) {
                    System.err.println("Read error: Missing Y coordinate data for point " + (i + 1));
                    return null;
                }
                int y = scanner.nextInt();

                points.add(new Point(x, y));
            }

            return points;

        } catch (FileNotFoundException e) {
            System.err.println("Read error: File not found " + fileName);
            return null;
        }
    }
}

