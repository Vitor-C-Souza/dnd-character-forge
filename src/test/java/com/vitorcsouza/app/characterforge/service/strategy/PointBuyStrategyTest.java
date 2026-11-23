package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PointBuyStrategyTest {
    @Test
    @DisplayName("Should generate AbilityScores when using full 27-point budget")
    void shouldGenerateWhenUsingFullBudget() {
        AbilityScores input = new AbilityScores(15, 14, 13, 12, 10, 8);

        PointBuyStrategy strategy = new PointBuyStrategy(input);
        AbilityScores result = strategy.generate();

        assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("Should generate AbilityScores when leaving unspent points within budget")
    void shouldGenerateWhenLeavingUnspentPoints() {
        AbilityScores input = new AbilityScores(15, 14, 13, 10, 10, 8);

        PointBuyStrategy strategy = new PointBuyStrategy(input);
        AbilityScores result = strategy.generate();

        assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("Should generate AbilityScores when all attributes are at minimum score 8")
    void shouldGenerateWhenAllAttributesAreMinimum() {
        AbilityScores input = new AbilityScores(8, 8, 8, 8, 8, 8);

        PointBuyStrategy strategy = new PointBuyStrategy(input);
        AbilityScores result = strategy.generate();

        assertThat(result).isEqualTo(input);
    }

    @ParameterizedTest(name = "Score {0} should cost {1} points")
    @CsvSource({
            "8, 0",
            "9, 1",
            "10, 2",
            "11, 3",
            "12, 4",
            "13, 5",
            "14, 7",
            "15, 9"
    })
    @DisplayName("Should calculate individual score costs accurately across table transitions")
    void shouldValidateIndividualScoreCosts(int testedScore, int expectedCost) {
        AbilityScores validAtBoundary = buildScoresWithTargetAndRemainingBudget(testedScore, 27 - expectedCost);

        PointBuyStrategy strategy = new PointBuyStrategy(validAtBoundary);

        assertThat(strategy.generate()).isEqualTo(validAtBoundary);
    }

    @Test
    @DisplayName("Should reject when any ability score is below minimum 8")
    void shouldRejectWhenScoreIsBelowMinimum() {
        AbilityScores input = new AbilityScores(7, 14, 13, 12, 10, 8);

        assertThatThrownBy(() -> new PointBuyStrategy(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 8 and 15");
    }

    @Test
    @DisplayName("Should reject when any ability score is above maximum 15")
    void shouldRejectWhenScoreIsAboveMaximum() {
        AbilityScores input = new AbilityScores(16, 12, 12, 10, 10, 8);

        assertThatThrownBy(() -> new PointBuyStrategy(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 8 and 15");
    }

    @Test
    @DisplayName("Should reject when total cost exceeds 27-point budget")
    void shouldRejectWhenBudgetIsExceeded() {
        AbilityScores input = new AbilityScores(15, 15, 15, 9, 8, 8);

        assertThatThrownBy(() -> new PointBuyStrategy(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("budget exceeded");
    }

    @Test
    @DisplayName("Should reject when AbilityScores is null")
    void shouldRejectWhenAbilityScoresIsNull() {
        assertThatThrownBy(() -> new PointBuyStrategy(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("AbilityScores cannot be null");
    }

    private AbilityScores buildScoresWithTargetAndRemainingBudget(int targetScore, int remainingPoints) {
        int[] others = {8, 8, 8, 8, 8};
        int[] availableValues = {15, 14, 13, 12, 11, 10, 9};
        int[] availableCosts = {9, 7, 5, 4, 3, 2, 1};

        int left = remainingPoints;
        int idx = 0;
        while (left > 0 && idx < others.length) {
            for (int i = 0; i < availableValues.length; i++) {
                if (availableCosts[i] <= left) {
                    others[idx++] = availableValues[i];
                    left -= availableCosts[i];
                    break;
                }
            }
        }
        return new AbilityScores(targetScore, others[0], others[1], others[2], others[3], others[4]);
    }

}