package org.example.domain.service;

import org.example.domain.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointGenerator {
    private final Random random;
    private final int maxCoordinate;

    public PointGenerator() {
        this(new Random(), 3000);
    }

    public PointGenerator(long seed) {
        this(new Random(seed), 3000);
    }

    public PointGenerator(long seed, int maxCoordinate) {
        this(new Random(seed), maxCoordinate);
    }

    public PointGenerator(Random random, int maxCoordinate) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        if (maxCoordinate <= 0) {
            throw new IllegalArgumentException("Max coordinate must be positive");
        }
        this.random = random;
        this.maxCoordinate = maxCoordinate;
    }

    public List<Point> generate() {
        int numberOfPoints = random.nextInt(50) + 1;
        return generate(numberOfPoints);
    }

    public List<Point> generate(int numberOfPoints) {
        if (numberOfPoints <= 0) {
            throw new IllegalArgumentException("Number of points must be positive");
        }

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
