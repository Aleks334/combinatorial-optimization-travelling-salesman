package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Tour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyTspSolver implements TspSolver {

    public Tour solve(List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            throw new IllegalArgumentException("Cities list cannot be empty");
        }

        List<City> tourOrder = new ArrayList<>();
        Set<City> unvisited = new HashSet<>(cities);
        
        City current = cities.getFirst();
        tourOrder.add(current);
        unvisited.remove(current);

        while (!unvisited.isEmpty()) {
            City nearest = findNearestCity(current, unvisited);
            tourOrder.add(nearest);
            unvisited.remove(nearest);
            current = nearest;
        }

        return new Tour(tourOrder);
    }

    private City findNearestCity(City current, Set<City> unvisited) {
        City nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (City city : unvisited) {
            double distance = current.distanceTo(city);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = city;
            }
        }

        return nearest;
    }
}
