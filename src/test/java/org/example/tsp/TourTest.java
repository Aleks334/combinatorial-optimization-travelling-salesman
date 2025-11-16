package org.example.tsp;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    @Test
    void testTotalDistanceCalculation() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 3, 0),
            new City("C", 3, 4)
        );
        
        Tour tour = new Tour(cities);
        assertEquals(12.0, tour.getTotalDistance(), 0.001);
    }

    @Test
    void testTourWithSingleCity() {
        List<City> cities = Arrays.asList(
            new City("A", 5, 5)
        );

        Tour tour = new Tour(cities);
        assertEquals(0.0, tour.getTotalDistance(), 0.001,
            "Trasa z jednym miastem powinna mieć dystans 0");
    }

    @Test
    void testTourWithTwoCities() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0)
        );

        Tour tour = new Tour(cities);
        assertEquals(20.0, tour.getTotalDistance(), 0.001,
            "Trasa A->B->A = 10+10 = 20");
    }

    @Test
    void testTourWithSquareCities() {
        // Kwadrat o boku 10
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 10, 0),
            new City("C", 10, 10),
            new City("D", 0, 10)
        );

        Tour tour = new Tour(cities);
        assertEquals(40.0, tour.getTotalDistance(), 0.001,
            "Obwód kwadratu 10x10 to 40");
    }

    @Test
    void testGetCities() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 3, 0),
            new City("C", 3, 4)
        );

        Tour tour = new Tour(cities);
        List<City> tourCities = tour.getCities();

        assertEquals(3, tourCities.size());
        assertEquals("A", tourCities.get(0).getName());
        assertEquals("B", tourCities.get(1).getName());
        assertEquals("C", tourCities.get(2).getName());
    }

    @Test
    void testToString() {
        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 3, 0)
        );

        Tour tour = new Tour(cities);
        String tourString = tour.toString();

        assertTrue(tourString.contains("A"));
        assertTrue(tourString.contains("B"));
        assertTrue(tourString.contains("->"));
        assertTrue(tourString.contains("Distance"));
    }
}
