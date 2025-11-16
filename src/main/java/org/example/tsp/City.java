package org.example.tsp;

import org.example.Point;

public class City extends Point {
    private final String name;

    public City(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
