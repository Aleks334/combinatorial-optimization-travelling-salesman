package org.example.antColony;

import org.example.Point;
import org.example.PointGenerator;
import org.example.tsp.City;
import org.example.tsp.Tour;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AntColonyTspSolverTest {

    @Test
    void testSolveSimpleSquare() {
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(100, 0),
            new Point(100, 100),
            new Point(0, 100)
        );

        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(4, tour.getCities().size());
        assertTrue(tour.getTotalDistance() > 0);

        assertTrue(tour.getTotalDistance() <= 450,
            "Długość trasy powinna być bliska optymalnej (400): " + tour.getTotalDistance());

        System.out.println("Test prosty kwadrat - Długość trasy: " + tour.getTotalDistance());
        System.out.println("Czas wykonania: " + solver.getExecutionTimeMs() + " ms");
    }

    @Test
    void testSolveSingleCity() {
        List<Point> points = Arrays.asList(new Point(50, 50));
        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(1, tour.getCities().size());
        assertEquals(0.0, tour.getTotalDistance(), 0.001);
    }

    @Test
    void testSolveThreeCitiesInLine() {
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0),
            new Point(20, 0)
        );
        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(3, tour.getCities().size());
        // Optimalna trasa: 0->10->20->0 = 10+10+20 = 40
        assertTrue(tour.getTotalDistance() >= 40.0 && tour.getTotalDistance() <= 45.0,
            "Dla 3 miast w linii optymalna długość to 40, otrzymano: " + tour.getTotalDistance());

        System.out.println("Test 3 miasta w linii - Długość: " + tour.getTotalDistance());
    }

    @Test
    void testSolveRandomInstanceSmall() {
        PointGenerator generator = new PointGenerator(12345L);
        List<Point> points = generator.generate(10);

        List<City> cities = convertPointsToCities(points);

        long startTime = System.currentTimeMillis();
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);
        long endTime = System.currentTimeMillis();

        assertNotNull(tour);
        assertEquals(10, tour.getCities().size());
        assertTrue(tour.getTotalDistance() > 0);

        System.out.println("\n=== Test mała instancja (10 miast, seed=12345) ===");
        System.out.println("Długość trasy: " + tour.getTotalDistance());
        System.out.println("Iteracje: " + solver.getIterationsCompleted());
        System.out.println("Czas: " + solver.getExecutionTimeMs() + " ms");
    }

    @Test
    void testSolveRandomInstanceMedium() {
        PointGenerator generator = new PointGenerator(54321L);
        List<Point> points = generator.generate(25);

        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(25, tour.getCities().size());
        assertTrue(tour.getTotalDistance() > 0);

        System.out.println("\n=== Test średnia instancja (25 miast, seed=54321) ===");
        System.out.println("Długość trasy: " + tour.getTotalDistance());
        System.out.println("Iteracje: " + solver.getIterationsCompleted());
        System.out.println("Czas: " + solver.getExecutionTimeMs() + " ms");
    }

    @Test
    void testSolveRandomInstanceLarge() {
        PointGenerator generator = new PointGenerator(99999L);
        List<Point> points = generator.generate(50);

        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(50, tour.getCities().size());
        assertTrue(tour.getTotalDistance() > 0);

        System.out.println("\n=== Test duża instancja (50 miast, seed=99999) ===");
        System.out.println("Długość trasy: " + tour.getTotalDistance());
        System.out.println("Iteracje: " + solver.getIterationsCompleted());
        System.out.println("Czas: " + solver.getExecutionTimeMs() + " ms");
    }

    @Test
    void testTimeConstraint() {
        PointGenerator generator = new PointGenerator(88888L);
        List<Point> points = generator.generate(50);

        List<City> cities = convertPointsToCities(points);

        long startTime = System.currentTimeMillis();
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);
        long endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;

        assertNotNull(tour);
        assertTrue(executionTime < 180000,
            "Czas wykonania powinien być < 3 minuty (180000 ms), był: " + executionTime + " ms");

        System.out.println("\n=== Test ograniczenia czasowego (50 miast) ===");
        System.out.println("Długość trasy: " + tour.getTotalDistance());
        System.out.println("Czas wykonania: " + (executionTime / 1000.0) + " s");
        System.out.println("Iteracje: " + solver.getIterationsCompleted());
    }

    @Test
    void testDeterministicInstanceGeneration() {
        long seed = 11111L;

        PointGenerator generator1 = new PointGenerator(seed);
        List<Point> points1 = generator1.generate(15);

        PointGenerator generator2 = new PointGenerator(seed);
        List<Point> points2 = generator2.generate(15);

        assertEquals(points1.size(), points2.size());
        for (int i = 0; i < points1.size(); i++) {
            assertEquals(points1.get(i).getX(), points2.get(i).getX());
            assertEquals(points1.get(i).getY(), points2.get(i).getY());
        }

        System.out.println("\n=== Test deterministyczności generatora ===");
        System.out.println("Instancje są identyczne dla tego samego seed: " + seed);
    }

    @Test
    void testRepeatedExecutionsSameSeed() {
        long seed = 22222L;
        PointGenerator generator = new PointGenerator(seed);
        List<Point> points = generator.generate(15);
        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver1 = new AntColonyTspSolver(points);
        Tour tour1 = solver1.solve(cities);

        AntColonyTspSolver solver2 = new AntColonyTspSolver(points);
        Tour tour2 = solver2.solve(cities);

        System.out.println("\n=== Test powtarzalności algorytmu (ta sama instancja) ===");
        System.out.println("Wynik 1: " + tour1.getTotalDistance() + " (iteracje: " + solver1.getIterationsCompleted() + ")");
        System.out.println("Wynik 2: " + tour2.getTotalDistance() + " (iteracje: " + solver2.getIterationsCompleted() + ")");

        assertTrue(tour1.getTotalDistance() > 0);
        assertTrue(tour2.getTotalDistance() > 0);
    }

    @Test
    void testTourValidityAllCitiesVisited() {
        PointGenerator generator = new PointGenerator(77777L);
        List<Point> points = generator.generate(20);
        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertEquals(cities.size(), tour.getCities().size(),
            "Trasa powinna zawierać wszystkie miasta");

        List<City> tourCities = tour.getCities();
        for (int i = 0; i < tourCities.size(); i++) {
            for (int j = i + 1; j < tourCities.size(); j++) {
                assertNotEquals(tourCities.get(i).getName(), tourCities.get(j).getName(),
                    "Trasa nie powinna zawierać duplikatów miast");
            }
        }

        System.out.println("\n=== Test poprawności trasy (20 miast) ===");
        System.out.println("Wszystkie miasta odwiedzone bez duplikatów: OK");
        System.out.println("Długość trasy: " + tour.getTotalDistance());
    }

    @Test
    void testStagnationDetection() {
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0),
            new Point(5, 8)
        );
        List<City> cities = convertPointsToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);

        System.out.println("\n=== Test detekcji stagnacji (3 miasta) ===");
        System.out.println("Iteracje wykonane: " + solver.getIterationsCompleted());
        System.out.println("Długość trasy: " + tour.getTotalDistance());
        System.out.println("Czas: " + solver.getExecutionTimeMs() + " ms");
    }

    private List<City> convertPointsToCities(List<Point> points) {
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            String cityName = "City" + (i + 1);
            cities.add(new City(cityName, point.getX(), point.getY()));
        }
        return cities;
    }
}

