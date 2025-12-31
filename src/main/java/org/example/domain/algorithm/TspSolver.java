package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Tour;

import java.util.List;

public interface TspSolver {
    Tour solve(List<City> cities);
}
