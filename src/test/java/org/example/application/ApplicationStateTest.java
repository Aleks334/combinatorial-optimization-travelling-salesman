package org.example.application;

import org.example.domain.algorithm.Algorithm;
import org.example.domain.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationStateTest {

    private ApplicationState state;

    @BeforeEach
    void setUp() {
        state = new ApplicationState();
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        // given

        // when

        // then
        assertThat(state.getPoints()).isNull();
        assertThat(state.getSelectedAlgorithm()).isEqualTo(Algorithm.ANT_COLONY);
        assertThat(state.shouldExit()).isFalse();
        assertThat(state.hasPoints()).isFalse();
    }

    @Test
    void shouldDetectPointsAvailability() {
        // given
        List<Point> points = List.of(new Point(5, 6));

        // when
        state.setPoints(points);

        // then
        assertThat(state.hasPoints()).isTrue();
    }

    @Test
    void shouldDetectEmptyPoints() {
        // given

        // when
        state.setPoints(Collections.emptyList());

        // then
        assertThat(state.hasPoints()).isFalse();
    }

    @Test
    void shouldDetectNullPoints() {
        // given

        // when
        state.setPoints(null);

        // then
        assertThat(state.hasPoints()).isFalse();
    }

    @Test
    void shouldChangeAlgorithm() {
        // given

        // when
        state.setSelectedAlgorithm(Algorithm.GREEDY);

        // then
        assertThat(state.getSelectedAlgorithm()).isEqualTo(Algorithm.GREEDY);
    }

    @Test
    void shouldSetAppStateToExit() {
        // given

        // when
        state.exit();

        // then
        assertThat(state.shouldExit()).isTrue();
    }
}
