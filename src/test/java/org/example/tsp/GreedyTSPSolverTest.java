package org.example.tsp;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreedyTSPSolverTest {

    @Test
    void testSquare() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0),
            new City("C", 10, 10),
            new City("D", 0, 10)
        );
        
        GreedyTspSolver solver = new GreedyTspSolver();
        Tour tour = solver.solve(cities);
        
        List<City> tourCities = tour.getCities();
        assertEquals(4, tourCities.size());
        assertEquals("A", tourCities.get(0).getName());
        assertEquals(40.0, tour.getTotalDistance(), 0.001);
    }

    @Test
    void testTriangle() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 5, 0),
            new City("C", 2, 4)
        );
        
        GreedyTspSolver solver = new GreedyTspSolver();
        Tour tour = solver.solve(cities);
        
        List<City> tourCities = tour.getCities();
        assertEquals(3, tourCities.size());
        assertEquals("A", tourCities.get(0).getName());
        assertTrue(tour.getTotalDistance() > 0);
    }

    @Test
    void testLinearCities() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 1, 0),
            new City("C", 2, 0),
            new City("D", 3, 0)
        );
        
        GreedyTspSolver solver = new GreedyTspSolver();
        Tour tour = solver.solve(cities);
        
        List<City> tourCities = tour.getCities();
        assertEquals("A", tourCities.get(0).getName());
        assertEquals("B", tourCities.get(1).getName());
        assertEquals("C", tourCities.get(2).getName());
        assertEquals("D", tourCities.get(3).getName());
        assertEquals(6.0, tour.getTotalDistance(), 0.001);
    }
}
