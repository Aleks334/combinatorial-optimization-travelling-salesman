package org.example.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class TourTest {

    @Nested
    class CoreFunctionality {

        @Test
        void shouldCalculateDistanceForTriangle() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4)
            );

            // when
            Tour tour = new Tour(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(12.0, within(0.001));
        }

        @Test
        void shouldReturnZeroDistanceForSingleCity() {
            // given
            List<City> cities = Arrays.asList(new City("A", 5, 5));

            // when
            Tour tour = new Tour(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(0.0, within(0.001));
        }

        @Test
        void shouldCalculateRoundTripDistanceForTwoCities() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0)
            );

            // when
            Tour tour = new Tour(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(20.0, within(0.001));
        }

        @Test
        void shouldReturnCitiesInCorrectOrder() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4)
            );
            Tour tour = new Tour(cities);

            // when
            List<City> tourCities = tour.getCities();

            // then
            assertThat(tourCities).hasSize(3);
            assertThat(tourCities.get(0).getName()).isEqualTo("A");
            assertThat(tourCities.get(1).getName()).isEqualTo("B");
            assertThat(tourCities.get(2).getName()).isEqualTo("C");
        }

        @Test
        void shouldFormatToString() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4)
            );
            Tour tour = new Tour(cities);

            // when
            String tourString = tour.toString();

            // then
            assertThat(tourString).contains("A", "B", "C", "->", "Distance");
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowExceptionForNullCities() {
            // given
            List<City> nullCities = null;

            // when & then
            assertThatThrownBy(() -> new Tour(nullCities))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldThrowExceptionForEmptyCities() {
            // given
            List<City> emptyCities = new ArrayList<>();

            // when & then
            assertThatThrownBy(() -> new Tour(emptyCities))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
