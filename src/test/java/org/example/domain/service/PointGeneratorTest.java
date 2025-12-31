package org.example.domain.service;

import org.example.domain.model.Point;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointGeneratorTest {

    @Nested
    class PointGeneration {

        @Test
        void shouldGenerateRandomNumberOfPoints() {
            // given
            PointGenerator generator = new PointGenerator();

            // when
            List<Point> points = generator.generate();

            // then
            assertThat(points).isNotEmpty();
            assertThat(points.size()).isLessThanOrEqualTo(50);
        }

        @ParameterizedTest(name = "Generate {0} points")
        @ValueSource(ints = {1, 5, 10, 25, 50, 100})
        void shouldGenerateSpecificNumberOfPoints(int count) {
            // given
            PointGenerator generator = new PointGenerator();

            // when
            List<Point> points = generator.generate(count);

            // then
            assertThat(points).hasSize(count);
        }

        @ParameterizedTest(name = "Generate {1} points with max coordinate {0}")
        @CsvSource({
            "100, 10",
            "500, 20",
            "1000, 50",
            "3000, 100"
        })
        void shouldRespectCustomMaxCoordinate(int maxCoordinate, int pointCount) {
            // given
            PointGenerator generator = new PointGenerator(123L, maxCoordinate);

            // when
            List<Point> points = generator.generate(pointCount);

            // then
            for (Point point : points) {
                assertThat(point.getX()).isBetween(0, maxCoordinate - 1);
                assertThat(point.getY()).isBetween(0, maxCoordinate - 1);
            }
        }
    }

    @Nested
    class Uniqueness {

        @ParameterizedTest(name = "Test uniqueness for {0} points")
        @ValueSource(ints = {10, 20, 30, 50})
        void shouldGenerateUniquePoints(int count) {
            // given
            PointGenerator generator = new PointGenerator();

            // when
            List<Point> points = generator.generate(count);

            // then
            Set<Point> uniquePoints = new HashSet<>(points);
            assertThat(uniquePoints).hasSize(points.size());
        }
    }

    @Nested
    class Determinism {

        @ParameterizedTest(name = "Seed {0}")
        @ValueSource(longs = {12345L, 54321L, 99999L})
        void shouldProduceSameResultsWithSameSeed(long seed) {
            // given
            PointGenerator generator1 = new PointGenerator(seed);
            PointGenerator generator2 = new PointGenerator(seed);

            // when
            List<Point> points1 = generator1.generate(10);
            List<Point> points2 = generator2.generate(10);

            // then
            assertPointsEqual(points1, points2);
        }
    }

    @Nested
    class Validation {

        @ParameterizedTest(name = "Invalid count: {0}")
        @ValueSource(ints = {0, -1, -5, -100})
        void shouldThrowExceptionForInvalidPointCount(int invalidCount) {
            // given
            PointGenerator generator = new PointGenerator();

            // when & then
            assertThatThrownBy(() -> generator.generate(invalidCount))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldThrowExceptionForNullRandom() {
            // given
            Random nullRandom = null;
            int maxCoordinate = 3000;

            // when & then
            assertThatThrownBy(() -> new PointGenerator(nullRandom, maxCoordinate))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "Invalid max coordinate: {0}")
        @ValueSource(ints = {0, -1, -100})
        void shouldThrowExceptionForInvalidMaxCoordinate(int invalidMax) {
            // given
            Random random = new Random();

            // when & then
            assertThatThrownBy(() -> new PointGenerator(random, invalidMax))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private void assertPointsEqual(List<Point> points1, List<Point> points2) {
        assertThat(points1).hasSameSizeAs(points2);
        for (int i = 0; i < points1.size(); i++) {
            assertThat(points1.get(i).getX()).isEqualTo(points2.get(i).getX());
            assertThat(points1.get(i).getY()).isEqualTo(points2.get(i).getY());
        }
    }
}
