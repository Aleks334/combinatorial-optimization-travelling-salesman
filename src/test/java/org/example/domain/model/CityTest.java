package org.example.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CityTest {

    @Nested
    class DistanceCalculation {

        @Test
        void shouldCalculateDistanceBetweenTwoCities() {
            // given
            City city1 = new City("A", 0, 0);
            City city2 = new City("B", 3, 4);

            // when
            double distance = city1.distanceTo(city2);

            // then
            assertThat(distance).isCloseTo(5.0, within(0.001));
        }

        @Test
        void shouldReturnZeroDistanceToSelf() {
            // given
            City city = new City("A", 5, 5);

            // when
            double distance = city.distanceTo(city);

            // then
            assertThat(distance).isCloseTo(0.0, within(0.001));
        }
    }
}
