package org.example.domain.service;

import org.example.domain.model.City;
import org.example.domain.model.Point;

import java.util.ArrayList;
import java.util.List;

public class CityConverter {

    private CityConverter() {
    }

    public static List<City> fromPoints(List<Point> points) {
        if (points == null) {
            throw new IllegalArgumentException("Points list cannot be null");
        }

        List<City> cities = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            String cityName = "City" + (i + 1);
            cities.add(new City(cityName, point.getX(), point.getY()));
        }
        return cities;
    }
}


