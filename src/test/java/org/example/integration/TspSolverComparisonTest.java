package org.example.integration;

import org.example.Point;
import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.GreedyTspSolver;
import org.example.tsp.Tour;
import org.example.tsp.TspSolver;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy integracyjne porównujące algorytm mrówkowy z algorytmem zachłannym
 */
class TspSolverComparisonTest {

    @Test
    void testCompareAlgorithmsOnSimpleCase() {
        // Test porównujący oba algorytmy na prostym przypadku
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0),
            new Point(10, 10),
            new Point(0, 10)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0),
            new City("C", 10, 10),
            new City("D", 0, 10)
        );

        // Algorytm mrówkowy
        TspSolver antColonySolver = new AntColonyTspSolver(points);
        Tour antColonyTour = antColonySolver.solve(cities);

        // Algorytm zachłanny
        TspSolver greedySolver = new GreedyTspSolver();
        Tour greedyTour = greedySolver.solve(cities);

        // Oba algorytmy powinny znaleźć rozwiązanie
        assertNotNull(antColonyTour);
        assertNotNull(greedyTour);

        // Obie trasy powinny zawierać wszystkie miasta
        assertEquals(4, antColonyTour.getCities().size());
        assertEquals(4, greedyTour.getCities().size());

        // Dla kwadratu optymalna trasa to 40
        assertEquals(40.0, antColonyTour.getTotalDistance(), 0.01);

        System.out.println("Algorytm mrówkowy - dystans: " + antColonyTour.getTotalDistance());
        System.out.println("Algorytm zachłanny - dystans: " + greedyTour.getTotalDistance());
    }

    @Test
    void testBothAlgorithmsProduceValidTours() {
        // Test sprawdzający, czy oba algorytmy produkują poprawne trasy
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(20, 10),
            new Point(15, 30),
            new Point(5, 25),
            new Point(10, 15)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 20, 10),
            new City("C", 15, 30),
            new City("D", 5, 25),
            new City("E", 10, 15)
        );

        // Algorytm mrówkowy
        TspSolver antColonySolver = new AntColonyTspSolver(points);
        Tour antColonyTour = antColonySolver.solve(cities);

        // Algorytm zachłanny
        TspSolver greedySolver = new GreedyTspSolver();
        Tour greedyTour = greedySolver.solve(cities);

        // Weryfikacja poprawności tras
        assertValidTour(antColonyTour, cities);
        assertValidTour(greedyTour, cities);

        // Algorytm mrówkowy powinien być konkurencyjny lub lepszy
        // (może być gorszy dla małych instancji ze względu na losowość)
        assertTrue(antColonyTour.getTotalDistance() > 0);
        assertTrue(greedyTour.getTotalDistance() > 0);

        System.out.println("\nTest z 5 miastami:");
        System.out.println("Algorytm mrówkowy: " + antColonyTour.getTotalDistance());
        System.out.println("Algorytm zachłanny: " + greedyTour.getTotalDistance());
    }

    @Test
    void testAlgorithmsHandleEdgeCases() {
        // Test przypadków brzegowych

        // Przypadek 1: Jedno miasto
        List<Point> points1 = Arrays.asList(new Point(5, 5));
        List<City> cities1 = Arrays.asList(new City("A", 5, 5));

        TspSolver solver1 = new AntColonyTspSolver(points1);
        Tour tour1 = solver1.solve(cities1);

        assertEquals(0.0, tour1.getTotalDistance(), 0.001);

        // Przypadek 2: Dwa miasta
        List<Point> points2 = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0)
        );
        List<City> cities2 = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0)
        );

        TspSolver solver2 = new AntColonyTspSolver(points2);
        Tour tour2 = solver2.solve(cities2);

        assertEquals(20.0, tour2.getTotalDistance(), 0.001);
    }

    /**
     * Pomocnicza metoda weryfikująca poprawność trasy
     */
    private void assertValidTour(Tour tour, List<City> originalCities) {
        assertNotNull(tour, "Trasa nie powinna być null");

        List<City> tourCities = tour.getCities();
        assertEquals(originalCities.size(), tourCities.size(),
            "Trasa powinna zawierać wszystkie miasta");

        // Sprawdź czy każde miasto występuje dokładnie raz
        for (City city : originalCities) {
            long count = tourCities.stream()
                .filter(c -> c.getName().equals(city.getName()))
                .count();
            assertEquals(1, count,
                "Każde miasto powinno wystąpić dokładnie raz w trasie");
        }

        assertTrue(tour.getTotalDistance() > 0 || originalCities.size() == 1,
            "Dystans powinien być większy od 0 (chyba że jedno miasto)");
    }
}

