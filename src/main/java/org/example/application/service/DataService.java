package org.example.application.service;

import org.example.application.port.out.PointRepository;
import org.example.domain.model.Point;
import org.example.domain.service.PointGenerator;

import java.util.List;
import java.util.Objects;

public class DataService {
    private final PointRepository repository;
    private final PointGenerator generator;

    public DataService(PointRepository repository) {
        this.repository = Objects.requireNonNull(repository);
        this.generator = new PointGenerator();
    }

    public List<Point> loadFromFile() throws Exception {
        return repository.loadPoints();
    }

    public List<Point> generateRandom(int count) {
        if (count > 0) {
            return generator.generate(count);
        }
        return generator.generate();
    }

    public void saveToFile(List<Point> points) throws Exception {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("No points to save");
        }
        repository.savePoints(points);
    }
}