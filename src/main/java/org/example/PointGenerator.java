package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointGenerator {
    private final Random random;
    private final int maxCoordinate;

    public PointGenerator() {
        this(new Random(), 3000);
    }

    public PointGenerator(Random random, int maxCoordinate) {
        this.random = random;
        this.maxCoordinate = maxCoordinate;
    }

    public List<Point> generate() {
        int numberOfPoints = random.nextInt(50) + 1;
        return generate(numberOfPoints);
    }

    public List<Point> generate(int numberOfPoints) {
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < numberOfPoints; i++) {
            Point point;
            do {
                int x = random.nextInt(maxCoordinate);
                int y = random.nextInt(maxCoordinate);
                point = new Point(x, y);
            } while (points.contains(point));

            points.add(point);
        }

        return points;
    }
}
