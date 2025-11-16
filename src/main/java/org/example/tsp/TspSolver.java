package org.example.tsp;

import java.util.List;

public interface TspSolver {
    Tour solve(List<City> cities);
}
