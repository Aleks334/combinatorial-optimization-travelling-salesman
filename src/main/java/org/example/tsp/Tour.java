package org.example.tsp;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private final List<City> cities;
    private final double totalDistance;

    public Tour(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        this.totalDistance = calculateTotalDistance();
    }

    private double calculateTotalDistance() {
        double distance = 0.0;
        
        for (int i = 0; i < cities.size() - 1; i++) {
            distance += cities.get(i).distanceTo(cities.get(i + 1));
        }
        
        distance += cities.get(cities.size() - 1).distanceTo(cities.get(0));
        
        return distance;
    }

    public List<City> getCities() {
        return cities;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cities.size(); i++) {
            sb.append(cities.get(i).getName());
            if (i < cities.size() - 1) {
                sb.append(" -> ");
            }
        }
        sb.append(" -> ").append(cities.get(0).getName());
        sb.append(String.format(" (Distance: %.2f)", totalDistance));
        return sb.toString();
    }
}
