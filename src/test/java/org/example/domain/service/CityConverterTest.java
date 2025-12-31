package org.example.domain.service;

import org.example.domain.model.City;
import org.example.domain.model.Point;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CityConverterTest {

    @Nested
    class ValidConversions {

        @Test
        void shouldConvertPointsToCitiesWithCorrectData() {
            // given
            List<Point> points = Arrays.asList(
                new Point(10, 20),
                new Point(30, 40)
            );

            // when
            List<City> cities = CityConverter.fromPoints(points);

            // then
            assertThat(cities).hasSize(2);
            assertThat(cities.get(0).getName()).isEqualTo("City1");
            assertThat(cities.get(0).getX()).isEqualTo(10);
            assertThat(cities.get(0).getY()).isEqualTo(20);
            assertThat(cities.get(1).getName()).isEqualTo("City2");
            assertThat(cities.get(1).getX()).isEqualTo(30);
            assertThat(cities.get(1).getY()).isEqualTo(40);
        }

        @Test
        void shouldConvertEmptyListToEmptyCitiesList() {
            // given
            List<Point> emptyPoints = Collections.emptyList();

            // when
            List<City> cities = CityConverter.fromPoints(emptyPoints);

            // then
            assertThat(cities).isEmpty();
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowExceptionForNullPoints() {
            // given
            List<Point> nullPoints = null;

            // when & then
            assertThatThrownBy(() -> CityConverter.fromPoints(nullPoints))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
