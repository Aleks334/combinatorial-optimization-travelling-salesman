package org.example.infrastructure.file;

import org.example.application.port.out.PointRepository;
import org.example.domain.model.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FilePointRepository implements PointRepository {
    private final String fileName;

    public FilePointRepository(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Point> loadPoints() throws DataException {
        File file = new File(fileName);
        if (!file.exists()) throw new DataException("File not found: " + fileName);

        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.hasNextInt()) {
                throw new DataException("Invalid format.");
            }

            int count = scanner.nextInt();
            List<Point> points = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                if (!scanner.hasNextInt())
                    break;

                scanner.nextInt();
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                points.add(new Point(x, y));
            }
            return points;
        } catch (Exception e) {
            throw new DataException("Error reading file", e);
        }
    }

    @Override
    public void savePoints(List<Point> points) throws DataException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(points.size());

            for (int i = 0; i < points.size(); i++) {
                Point p = points.get(i);
                writer.println((i + 1) + " " + p.getX() + " " + p.getY());
            }
        } catch (FileNotFoundException e) {
            throw new DataException("Cannot write to file", e);
        }
    }
}