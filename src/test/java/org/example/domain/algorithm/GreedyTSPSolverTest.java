package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Tour;
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

class GreedyTSPSolverTest {

    private final GreedyTspSolver solver = new GreedyTspSolver();

    @Nested
    class InputValidation {

        @Test
        void shouldThrowExceptionForNullCities() {
            // given
            List<City> nullCities = null;

            // when & then
            assertThatThrownBy(() -> solver.solve(nullCities))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldThrowExceptionForEmptyCities() {
            // given
            List<City> emptyCities = new ArrayList<>();

            // when & then
            assertThatThrownBy(() -> solver.solve(emptyCities))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class EdgeCases {

        @Test
        void shouldSolveSingleCity() {
            // given
            List<City> cities = List.of(new City("A", 0, 0));

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(1);
        }

        @Test
        void shouldReturnZeroDistanceForSingleCity() {
            // given
            List<City> cities = Arrays.asList(new City("A", 0, 0));

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(0.0, within(0.001));
        }

        @Test
        void shouldSolveTwoCities() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0)
            );

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(2);
        }

        @Test
        void shouldCalculateCorrectDistanceForTwoCities() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0)
            );

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(20.0, within(0.001));
        }
    }

    @Nested
    class BasicScenarios {

        @Test
        void shouldSolveSimpleTriangle() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4)
            );

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(3);
        }

        @Test
        void shouldCalculateReasonableDistanceForTriangle() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 3, 0),
                new City("C", 3, 4)
            );

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isLessThan(15.0);
        }

        @Test
        void shouldSolveSimpleSquare() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0),
                new City("C", 10, 10),
                new City("D", 0, 10)
            );

            // when
            Tour tour = solveAndValidate(cities, 4);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(40.0, within(0.1));
        }
    }

    @Nested
    class KnownShapes {

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.example.domain.algorithm.GreedyTSPSolverTest#knownShapeTestCases")
        void shouldSolveKnownShapes(String description, List<City> cities,
                            double expectedDistance, double tolerance) {
            // given

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(cities.size());
            assertThat(tour.getTotalDistance()).isCloseTo(expectedDistance, within(tolerance));
        }
    }

    static Stream<Object[]> knownShapeTestCases() {
        return Stream.of(
            new Object[]{"Line 3 cities", Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0),
                new City("C", 20, 0)
            ), 40.0, 0.1},

            new Object[]{"Square 10x10", Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0),
                new City("C", 10, 10),
                new City("D", 0, 10)
            ), 40.0, 0.1},

            new Object[]{"Rectangle 20x10", Arrays.asList(
                new City("A", 0, 0),
                new City("B", 20, 0),
                new City("C", 20, 10),
                new City("D", 0, 10)
            ), 60.0, 0.1}
        );
    }

    private Tour solveAndValidate(List<City> cities, int expectedCityCount) {
        Tour tour = solver.solve(cities);
        assertThat(tour).isNotNull();
        assertThat(tour.getCities()).hasSize(expectedCityCount);
        assertThat(tour.getTotalDistance()).isGreaterThan(0);
        return tour;
    }
}
