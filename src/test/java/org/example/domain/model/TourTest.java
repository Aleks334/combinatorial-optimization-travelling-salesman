package org.example.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class TourTest {

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

    @Nested
    class DistanceCalculation {

        @Test
        void shouldCalculateDistanceForSimpleTriangle() {
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

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.example.domain.model.TourTest#geometricShapeTestCases")
        void shouldCalculateCorrectDistancesForGeometricShapes(String shapeName, List<City> cities, double expectedDistance) {
            // given

            // when
            Tour tour = new Tour(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(expectedDistance, within(0.001));
        }
    }

    @Nested
    class TourProperties {

        private Tour tour;

        @BeforeEach
        void setUp() {
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4)
            );
            tour = new Tour(cities);
        }

        @Test
        void shouldReturnCitiesInCorrectOrder() {
            // given

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

            // when
            String tourString = tour.toString();

            // then
            assertThat(tourString).contains("A", "B", "C", "->", "Distance");
        }
    }

    static Stream<Object[]> geometricShapeTestCases() {
        return Stream.of(
            new Object[]{"Square 10x10", Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0),
                new City("C", 10, 10),
                new City("D", 0, 10)
            ), 40.0},

            new Object[]{"Square 5x5", Arrays.asList(
                new City("A", 0, 0),
                new City("B", 5, 0),
                new City("C", 5, 5),
                new City("D", 0, 5)
            ), 20.0},

            new Object[]{"Rectangle 3x4", Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4),
                new City("D", 0, 4)
            ), 14.0}
        );
    }
}
