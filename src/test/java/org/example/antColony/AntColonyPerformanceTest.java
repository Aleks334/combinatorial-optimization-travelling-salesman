package org.example.antColony;

import org.example.Point;
import org.example.tsp.City;
import org.example.tsp.Tour;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy wydajnościowe i rozszerzone dla algorytmu mrówkowego
 */
class AntColonyPerformanceTest {

    @Test
    @Timeout(30) // Test powinien zakończyć się w 30 sekund
    void testPerformanceWithMediumSizeInstance() {
        // Test z 15 miastami
        int numberOfCities = 15;
        List<Point> points = generateRandomPoints(numberOfCities, 100);
        List<City> cities = convertToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        long startTime = System.currentTimeMillis();

        Tour tour = solver.solve(cities);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertNotNull(tour);
        assertEquals(numberOfCities, tour.getCities().size());
        assertTrue(tour.getTotalDistance() > 0);

        System.out.println("\nWydajność dla 15 miast:");
        System.out.println("Czas wykonania: " + duration + " ms");
        System.out.println("Długość trasy: " + tour.getTotalDistance());
    }

    @Test
    void testDeterministicBehaviorWithSameSeed() {
        // Test sprawdzający czy algorytm z tym samym seedem daje podobne wyniki
        List<Point> points = generateRandomPoints(8, 50);
        List<City> cities = convertToCities(points);

        // Pierwsze uruchomienie
        AntColonyTspSolver solver1 = new AntColonyTspSolver(points);
        Tour tour1 = solver1.solve(cities);

        // Drugie uruchomienie
        AntColonyTspSolver solver2 = new AntColonyTspSolver(points);
        Tour tour2 = solver2.solve(cities);

        // Wyniki mogą się różnić ze względu na losowość, ale powinny być w podobnym zakresie
        double difference = Math.abs(tour1.getTotalDistance() - tour2.getTotalDistance());
        double averageDistance = (tour1.getTotalDistance() + tour2.getTotalDistance()) / 2;

        // Różnica nie powinna przekraczać 20% średniej
        assertTrue(difference < averageDistance * 0.2,
            "Różnica między uruchomieniami jest zbyt duża");

        System.out.println("\nTest powtarzalności:");
        System.out.println("Uruchomienie 1: " + tour1.getTotalDistance());
        System.out.println("Uruchomienie 2: " + tour2.getTotalDistance());
        System.out.println("Różnica: " + difference);
    }

    @Test
    void testConvergenceQuality() {
        // Test sprawdzający jakość zbieżności algorytmu
        // Używamy prostego przypadku gdzie znamy optymalną trasę

        // Kwadrat 20x20
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(20, 0));
        points.add(new Point(20, 20));
        points.add(new Point(0, 20));

        List<City> cities = convertToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        // Optymalna trasa = 80 (obwód kwadratu)
        double optimalDistance = 80.0;
        double foundDistance = tour.getTotalDistance();

        assertEquals(optimalDistance, foundDistance, 0.01,
            "Algorytm powinien znaleźć optymalną trasę dla kwadratu");

        System.out.println("\nTest jakości zbieżności (kwadrat):");
        System.out.println("Optymalna trasa: " + optimalDistance);
        System.out.println("Znaleziona trasa: " + foundDistance);
    }

    @Test
    void testScalability() {
        // Test skalowalności - sprawdzenie jak algorytm radzi sobie z różnymi rozmiarami
        int[] sizes = {5, 10, 15};

        for (int size : sizes) {
            List<Point> points = generateRandomPoints(size, 100);
            List<City> cities = convertToCities(points);

            AntColonyTspSolver solver = new AntColonyTspSolver(points);
            long startTime = System.currentTimeMillis();

            Tour tour = solver.solve(cities);

            long duration = System.currentTimeMillis() - startTime;

            assertNotNull(tour);
            assertEquals(size, tour.getCities().size());

            System.out.println("\nSkalowalność - " + size + " miast:");
            System.out.println("Czas: " + duration + " ms");
            System.out.println("Dystans: " + String.format("%.2f", tour.getTotalDistance()));
        }
    }

    @Test
    void testTriangleInequality() {
        // Test sprawdzający czy algorytm respektuje nierówność trójkąta
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(10, 0));
        points.add(new Point(5, 8));

        List<City> cities = convertToCities(points);

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        // Oblicz bezpośrednie odległości
        double d01 = points.get(0).distanceTo(points.get(1));
        double d12 = points.get(1).distanceTo(points.get(2));
        double d20 = points.get(2).distanceTo(points.get(0));

        double perimeter = d01 + d12 + d20;

        // Znaleziona trasa powinna być równa obwodowi trójkąta
        assertEquals(perimeter, tour.getTotalDistance(), 0.01,
            "Dla trzech miast trasa powinna równać się obwodowi trójkąta");

        System.out.println("\nTest nierówności trójkąta:");
        System.out.println("Obwód: " + String.format("%.2f", perimeter));
        System.out.println("Znaleziona trasa: " + String.format("%.2f", tour.getTotalDistance()));
    }

    // --- Metody pomocnicze ---

    /**
     * Generuje losowe punkty w zadanym zakresie
     */
    private List<Point> generateRandomPoints(int count, int maxCoordinate) {
        List<Point> points = new ArrayList<>();
        Random random = new Random(12345); // Stały seed dla powtarzalności

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(maxCoordinate);
            int y = random.nextInt(maxCoordinate);
            points.add(new Point(x, y));
        }

        return points;
    }

    /**
     * Konwertuje listę punktów na miasta
     */
    private List<City> convertToCities(List<Point> points) {
        List<City> cities = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            cities.add(new City("City" + (i + 1), p.getX(), p.getY()));
        }

        return cities;
    }
}

