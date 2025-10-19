package org.example.tsp;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
