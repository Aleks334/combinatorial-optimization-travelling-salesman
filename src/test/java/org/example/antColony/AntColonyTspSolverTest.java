package org.example.antColony;

import org.example.Point;
import org.example.tsp.City;
import org.example.tsp.Tour;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AntColonyTspSolverTest {

    @Test
    void testSolveWithSmallSetOfCities() {
        // Przygotowanie danych - 4 miasta w kwadracie
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

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        // Sprawdzenie podstawowych właściwości
        assertNotNull(tour, "Rozwiązanie nie powinno być null");
        assertEquals(4, tour.getCities().size(), "Trasa powinna zawierać wszystkie 4 miasta");
        assertTrue(tour.getTotalDistance() > 0, "Całkowity dystans powinien być większy od 0");

        // Dla kwadratu optymalna trasa to 40 (10+10+10+10)
        assertTrue(tour.getTotalDistance() >= 40.0,
            "Dystans powinien wynosić co najmniej 40 dla optymalnej trasy");
        assertTrue(tour.getTotalDistance() <= 60.0,
            "Dystans nie powinien przekraczać rozsądnej wartości dla 4 miast w kwadracie");
    }

    @Test
    void testSolveWithThreeCities() {
        // Test z trzema miastami w linii prostej
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(3, 0),
            new Point(6, 0)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 3, 0),
            new City("C", 6, 0)
        );

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(3, tour.getCities().size());

        // Optymalna trasa: A->B->C->A = 3+3+6 = 12
        assertTrue(tour.getTotalDistance() >= 12.0, "Minimalna trasa to 12");
        assertTrue(tour.getTotalDistance() <= 20.0, "Trasa powinna być bliska optymalnej");
    }

    @Test
    void testSolveWithTriangleCities() {
        // Test z trzema miastami w trójkącie prostokątnym
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(3, 0),
            new Point(3, 4)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 3, 0),
            new City("C", 3, 4)
        );

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(3, tour.getCities().size());

        // Optymalna trasa: A->B->C->A = 3+4+5 = 12
        assertEquals(12.0, tour.getTotalDistance(), 0.01,
            "Dla trójkąta prostokątnego (3,4,5) optymalna trasa to 12");
    }

    @Test
    void testAllCitiesVisited() {
        // Sprawdzenie, czy wszystkie miasta są odwiedzone dokładnie raz
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(5, 0),
            new Point(5, 5),
            new Point(0, 5),
            new Point(2, 2)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 5, 0),
            new City("C", 5, 5),
            new City("D", 0, 5),
            new City("E", 2, 2)
        );

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(5, tour.getCities().size(), "Wszystkie 5 miast powinno być w trasie");

        // Sprawdź czy każde miasto występuje dokładnie raz
        List<City> tourCities = tour.getCities();
        for (City originalCity : cities) {
            long count = tourCities.stream()
                .filter(c -> c.getName().equals(originalCity.getName()))
                .count();
            assertEquals(1, count,
                "Miasto " + originalCity.getName() + " powinno wystąpić dokładnie raz");
        }
    }

    @Test
    void testConsistentResults() {
        // Test sprawdzający, czy algorytm daje sensowne wyniki przy kilku uruchomieniach
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

        // Uruchom algorytm kilka razy
        double minDistance = Double.MAX_VALUE;
        double maxDistance = Double.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            AntColonyTspSolver solver = new AntColonyTspSolver(points);
            Tour tour = solver.solve(cities);
            double distance = tour.getTotalDistance();

            minDistance = Math.min(minDistance, distance);
            maxDistance = Math.max(maxDistance, distance);
        }

        // Wszystkie wyniki powinny być w rozsądnym zakresie
        assertTrue(minDistance >= 40.0, "Minimalna znaleziona trasa powinna być >= 40");
        assertTrue(maxDistance <= 60.0, "Maksymalna znaleziona trasa powinna być <= 60");
    }

    @Test
    void testSingleCity() {
        // Test z jednym miastem (krawędziowy przypadek)
        List<Point> points = Arrays.asList(new Point(5, 5));
        List<City> cities = Arrays.asList(new City("A", 5, 5));

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(1, tour.getCities().size());
        assertEquals(0.0, tour.getTotalDistance(), 0.001,
            "Trasa z jednym miastem powinna mieć dystans 0");
    }

    @Test
    void testTwoCities() {
        // Test z dwoma miastami
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0)
        );

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(2, tour.getCities().size());
        assertEquals(20.0, tour.getTotalDistance(), 0.001,
            "Trasa z 2 miastami: A->B->A = 10+10 = 20");
    }

    @Test
    void testLargerSetOfCities() {
        // Test z większą liczbą miast - 8 miast w siatce
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0),
            new Point(20, 0),
            new Point(0, 10),
            new Point(10, 10),
            new Point(20, 10),
            new Point(0, 20),
            new Point(10, 20)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0),
            new City("C", 20, 0),
            new City("D", 0, 10),
            new City("E", 10, 10),
            new City("F", 20, 10),
            new City("G", 0, 20),
            new City("H", 10, 20)
        );

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);
        assertEquals(8, tour.getCities().size(), "Wszystkie 8 miast powinno być w trasie");
        assertTrue(tour.getTotalDistance() > 0, "Dystans powinien być większy od 0");

        // Sprawdź, czy znaleziono rozsądne rozwiązanie
        // Minimalna możliwa trasa to obejście po obwodzie
        assertTrue(tour.getTotalDistance() < 200,
            "Trasa powinna być krótsza niż 200 dla 8 miast w siatce");
    }

    @Test
    void testAlgorithmFindsReasonableSolution() {
        // Test sprawdzający, czy algorytm znajduje rozwiązanie lepsze niż losowe
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(100, 0),
            new Point(100, 100),
            new Point(0, 100),
            new Point(50, 50)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 100, 0),
            new City("C", 100, 100),
            new City("D", 0, 100),
            new City("E", 50, 50)
        );

        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        assertNotNull(tour);

        // Optymalna trasa powinna być bliska obejścia kwadratu z odwiedzeniem środka
        // Najgorsza możliwa trasa byłaby znacznie dłuższa
        double distance = tour.getTotalDistance();
        assertTrue(distance > 200, "Dystans powinien być większy niż minimalna możliwa wartość");
        assertTrue(distance < 600, "Dystans nie powinien być zbyt duży");
    }
}

