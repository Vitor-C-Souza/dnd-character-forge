package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StandardArrayStrategyTest {

    private static Stream<Arguments> provideInvalidDistributions() {
        return Stream.of(
                Arguments.of(new AbilityScores(15, 15, 13, 12, 10, 8)),
                Arguments.of(new AbilityScores(15, 14, 13, 12, 10, 9)),
                Arguments.of(new AbilityScores(10, 10, 10, 10, 10, 10)),
                Arguments.of(new AbilityScores(18, 14, 13, 12, 10, 8))
        );
    }

    @Test
    @DisplayName("Should create strategy and return AbilityScores when valid Standard Array is provided in default order")
    void shouldAcceptValidStandardArrayInDefaultOrder() {
        AbilityScores validScores = new AbilityScores(15, 14, 13, 12, 10, 8);
        StandardArrayStrategy strategy = new StandardArrayStrategy(validScores);

        AbilityScores result = strategy.generate();

        assertThat(result).isEqualTo(validScores);
    }

    @Test
    @DisplayName("Should create strategy and return AbilityScores when valid Standard Array is provided in custom order")
    void shouldAcceptValidStandardArrayInCustomOrder() {
        AbilityScores rogueScores = new AbilityScores(8, 15, 14, 12, 13, 10);
        StandardArrayStrategy strategy = new StandardArrayStrategy(rogueScores);

        AbilityScores result = strategy.generate();

        assertThat(result).isEqualTo(rogueScores);
    }

    @Test
    @DisplayName("Should throw NullPointerException when scores is null")
    void shouldThrowExceptionWhenScoresIsNull() {
        assertThatThrownBy(() -> new StandardArrayStrategy(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("AbilityScores cannot be null");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDistributions")
    @DisplayName("Should throw IllegalArgumentException when distribution does not match Standard Array set")
    void shouldRejectInvalidDistributions(AbilityScores invalidScores) {
        assertThatThrownBy(() -> new StandardArrayStrategy(invalidScores))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid Standard Array distribution");
    }
}