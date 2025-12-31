package org.example.domain.algorithm;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlgorithmTest {

    @Test
    void shouldReturnGreedyForChoiceOne() {
        // given
        int choice = 1;

        // when
        Algorithm algorithm = Algorithm.fromChoice(choice);

        // then
        assertThat(algorithm).isEqualTo(Algorithm.GREEDY);
    }

    @Test
    void shouldReturnAntColonyForChoiceTwo() {
        // given
        int choice = 2;

        // when
        Algorithm algorithm = Algorithm.fromChoice(choice);

        // then
        assertThat(algorithm).isEqualTo(Algorithm.ANT_COLONY);
    }

    @Test
    void shouldDefaultToAntColonyForInvalidChoice() {
        // given
        int invalidChoice = 999;

        // when
        Algorithm algorithm = Algorithm.fromChoice(invalidChoice);

        // then
        assertThat(algorithm).isEqualTo(Algorithm.ANT_COLONY);
    }

    @Test
    void shouldDefaultToAntColonyForNegativeChoice() {
        // given
        int negativeChoice = -1;

        // when
        Algorithm algorithm = Algorithm.fromChoice(negativeChoice);

        // then
        assertThat(algorithm).isEqualTo(Algorithm.ANT_COLONY);
    }
}

