package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;
import org.example.domain.service.CityConverter;
import org.example.domain.service.PointGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class AntColonyTspSolverTest {

    @Nested
    class CoreFunctionality {

        @Test
        void shouldSolveSimpleProblem() {
            // given
            List<Point> points = Arrays.asList(
                    new Point(0, 0),
                    new Point(100, 0),
                    new Point(100, 100),
                    new Point(0, 100)
            );
            List<City> cities = CityConverter.fromPoints(points);
            AntColonyTspSolver solver = new AntColonyTspSolver(points);

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour).isNotNull();
            assertThat(tour.getCities()).hasSize(4);
            assertThat(tour.getTotalDistance()).isGreaterThan(0);
        }

        @Test
        void shouldProduceTourForRandomData() {
            // given
            PointGenerator generator = new PointGenerator(12345L);
            List<Point> points = generator.generate(20);
            List<City> cities = CityConverter.fromPoints(points);
            AntColonyTspSolver solver = new AntColonyTspSolver(points);

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isGreaterThan(0);
            assertThat(tour.getCities()).hasSize(20);
        }

        @Test
        void shouldHandleSingleCity() {
            // given
            List<Point> points = Arrays.asList(new Point(50, 50));
            List<City> cities = CityConverter.fromPoints(points);
            AntColonyTspSolver solver = new AntColonyTspSolver(points);

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(0.0, within(0.1));
        }

        @Test
        void shouldHandleTwoCities() {
            // given
            List<Point> points = Arrays.asList(
                    new Point(0, 0),
                    new Point(100, 0)
            );
            List<City> cities = CityConverter.fromPoints(points);
            AntColonyTspSolver solver = new AntColonyTspSolver(points);

            // when
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(200.0, within(0.1));
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowExceptionForEmptyPoints() {
            // given
            List<Point> emptyPoints = Collections.emptyList();

            // when & then
            assertThatThrownBy(() -> new AntColonyTspSolver(emptyPoints))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
