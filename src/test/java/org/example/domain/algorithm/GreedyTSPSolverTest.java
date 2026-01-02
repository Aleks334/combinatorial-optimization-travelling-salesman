package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Tour;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class GreedyTSPSolverTest {

    private final GreedyTspSolver solver = new GreedyTspSolver();

    @Nested
    class CoreFunctionality {

        @Test
        void shouldSolveSimpleCase() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0),
                new City("C", 10, 10),
                new City("D", 0, 10)
            );

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(4);
            assertThat(tour.getTotalDistance()).isCloseTo(40.0, within(0.1));
        }

        @Test
        void shouldHandleSingleCity() {
            // given
            List<City> cities = List.of(new City("A", 0, 0));

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(1);
            assertThat(tour.getTotalDistance()).isCloseTo(0.0, within(0.001));
        }

        @Test
        void shouldHandleTwoCities() {
            // given
            List<City> cities = Arrays.asList(
                new City("A", 0, 0),
                new City("B", 10, 0)
            );

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getCities()).hasSize(2);
            assertThat(tour.getTotalDistance()).isCloseTo(20.0, within(0.001));
        }
    }

    @Nested
    class Validation {

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
}
