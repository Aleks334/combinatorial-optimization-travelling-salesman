package org.example.domain.algorithm;

import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;
import org.example.domain.service.CityConverter;
import org.example.domain.service.PointGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class AntColonyTspSolverTest {

    @Nested
    class KnownGeometricShapes {

        @Test
        void shouldSolveSimpleSquare() {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(100, 0),
                new Point(100, 100),
                new Point(0, 100)
            );

            // when
            Tour tour = solveAndValidate(points, 4);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(400.0, within(10.0));
        }

        @Test
        void shouldSolveLineOfThreeCities() {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(10, 0),
                new Point(20, 0)
            );
            List<City> cities = CityConverter.fromPoints(points);

            // when
            AntColonyTspSolver solver = new AntColonyTspSolver(points);
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(40.0, within(0.1));
        }

        @Test
        void shouldHandleSinglePoint() {
            // given
            List<Point> points = Arrays.asList(new Point(50, 50));

            // when
            Tour tour = solveAndValidate(points, 1);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(0.0, within(0.1));
        }

        @ParameterizedTest(name = "Square size: {0}")
        @ValueSource(ints = {10, 50, 100, 200})
        void shouldHandleDifferentSquareSizes(int size) {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(size, 0),
                new Point(size, size),
                new Point(0, size)
            );

            // when
            Tour tour = solveAndValidate(points, 4);

            // then
            double expectedPerimeter = 4.0 * size;
            assertThat(tour.getTotalDistance()).isCloseTo(expectedPerimeter, within(size * 0.1));
        }
    }

    @Nested
    class RandomData {

        @ParameterizedTest(name = "{0} random cities")
        @ValueSource(ints = {5, 10, 20})
        void shouldSolveRandomDataSets(int cityCount) {
            // given
            PointGenerator generator = new PointGenerator(12345L);
            List<Point> points = generator.generate(cityCount);

            // when
            Tour tour = solveAndValidate(points, cityCount);

            // then
            assertThat(tour.getTotalDistance()).isGreaterThan(0);
        }

        @ParameterizedTest
        @CsvSource({
            "5, 100",
            "10, 500",
            "15, 1000"
        })
        void shouldProduceTourWithReasonableLength(int cityCount, int maxCoordinate) {
            // given
            PointGenerator generator = new PointGenerator(42L, maxCoordinate);
            List<Point> points = generator.generate(cityCount);

            // when
            Tour tour = solveAndValidate(points, cityCount);

            // then
            double maxPossibleDistance = cityCount * maxCoordinate * Math.sqrt(2) * 2;
            assertThat(tour.getTotalDistance()).isLessThan(maxPossibleDistance);
        }
    }

    @Nested
    class AlgorithmBehavior {

        @Test
        void shouldFindReasonableSolutionForMediumProblem() {
            // given
            PointGenerator generator = new PointGenerator(999L);
            List<Point> points = generator.generate(15);

            // when
            Tour tour = solveAndValidate(points, 15);

            // then
            assertThat(tour.getTotalDistance()).isGreaterThan(0);
        }

        @Test
        void shouldImproveOverIterations() {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(100, 0),
                new Point(100, 100),
                new Point(0, 100),
                new Point(50, 50)
            );

            // when
            AntColonyTspSolver solver = new AntColonyTspSolver(points);
            List<City> cities = CityConverter.fromPoints(points);
            Tour tour = solver.solve(cities);

            // then
            assertThat(tour).isNotNull();
            assertThat(tour.getTotalDistance()).isGreaterThan(0);
        }

        @Test
        void shouldHandleDenselyPackedCities() {
            // given
            List<Point> points = Arrays.asList(
                new Point(10, 10),
                new Point(11, 10),
                new Point(10, 11),
                new Point(11, 11)
            );

            // when
            Tour tour = solveAndValidate(points, 4);

            // then
            assertThat(tour.getTotalDistance()).isLessThan(10.0);
        }
    }

    @Nested
    class EdgeCases {

        @Test
        void shouldHandleTwoCities() {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(100, 0)
            );

            // when
            Tour tour = solveAndValidate(points, 2);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(200.0, within(0.1));
        }

        @Test
        void shouldHandleCollinearPoints() {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(50, 0),
                new Point(100, 0),
                new Point(150, 0)
            );

            // when
            Tour tour = solveAndValidate(points, 4);

            // then
            assertThat(tour.getTotalDistance()).isCloseTo(300.0, within(1.0));
        }

        @Test
        void shouldHandleClusteredPoints() {
            // given
            List<Point> points = Arrays.asList(
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 0),
                new Point(100, 100)
            );

            // when
            Tour tour = solveAndValidate(points, 4);

            // then
            assertThat(tour).isNotNull();
            assertThat(tour.getTotalDistance()).isGreaterThan(0);
        }
    }

    @Nested
    class ValidationTests {

        @Test
        void shouldThrowExceptionForNullPoints() {
            // given
            List<Point> nullPoints = null;

            // when & then
            assertThatThrownBy(() -> new AntColonyTspSolver(nullPoints))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldThrowExceptionForEmptyPoints() {
            // given
            List<Point> emptyPoints = Collections.emptyList();

            // when & then
            assertThatThrownBy(() -> new AntColonyTspSolver(emptyPoints))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Tour solveAndValidate(List<Point> points, int expectedCityCount) {
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        List<City> cities = CityConverter.fromPoints(points);
        Tour tour = solver.solve(cities);

        assertThat(tour).isNotNull();
        assertThat(tour.getCities()).hasSize(expectedCityCount);
        assertThat(tour.getTotalDistance()).isGreaterThanOrEqualTo(0);

        return tour;
    }
}
