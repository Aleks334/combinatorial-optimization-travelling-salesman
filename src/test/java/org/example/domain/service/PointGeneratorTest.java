package org.example.domain.service;

import org.example.domain.model.Point;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointGeneratorTest {

    @Nested
    class CoreFunctionality {

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

        @Test
        void shouldGenerateSpecificNumberOfPoints() {
            // given
            PointGenerator generator = new PointGenerator();

            // when
            List<Point> points = generator.generate(10);

            // then
            assertThat(points).hasSize(10);
        }

        @Test
        void shouldRespectCustomMaxCoordinate() {
            // given
            int maxCoordinate = 500;
            PointGenerator generator = new PointGenerator(123L, maxCoordinate);

            // when
            List<Point> points = generator.generate(20);

            // then
            for (Point point : points) {
                assertThat(point.getX()).isBetween(0, maxCoordinate - 1);
                assertThat(point.getY()).isBetween(0, maxCoordinate - 1);
            }
        }

        @Test
        void shouldGenerateUniquePoints() {
            // given
            PointGenerator generator = new PointGenerator();

            // when
            List<Point> points = generator.generate(20);

            // then
            Set<Point> uniquePoints = new HashSet<>(points);
            assertThat(uniquePoints).hasSize(points.size());
        }

        @Test
        void shouldProduceSameResultsWithSameSeed() {
            // given
            long seed = 12345L;
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

        @Test
        void shouldThrowExceptionForInvalidPointCount() {
            // given
            PointGenerator generator = new PointGenerator();

            // when & then
            assertThatThrownBy(() -> generator.generate(0))
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

        @Test
        void shouldThrowExceptionForInvalidMaxCoordinate() {
            // given
            Random random = new Random();

            // when & then
            assertThatThrownBy(() -> new PointGenerator(random, 0))
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
