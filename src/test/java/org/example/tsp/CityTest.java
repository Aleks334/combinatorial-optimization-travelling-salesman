package org.example.tsp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CityTest {

    @Test
    void testDistanceCalculation() {
        City city1 = new City("A", 0, 0);
        City city2 = new City("B", 3, 4);
        
        assertEquals(5.0, city1.distanceTo(city2), 0.001);
    }

    @Test
    void testDistanceToSelf() {
        City city = new City("A", 5, 5);
        assertEquals(0.0, city.distanceTo(city), 0.001);
    }
}
