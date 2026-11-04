package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;
import com.vitorcsouza.app.characterforge.service.strategy.AttributeGenerationMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AttributeGenerationServiceTest {
    private AttributeGenerationService service;

    @BeforeEach
    void setUp() {
        service = new AttributeGenerationService();
    }

    @Test
    @DisplayName("Should return valid distribution when STANDARD_ARRAY mode is used with valid scores")
    void shouldGenerateWithStandardArrayMode() {
        AbilityScores scores = new AbilityScores(15, 14, 13, 12, 10, 8);

        AbilityScores result = service.generate(AttributeGenerationMode.STANDARD_ARRAY, scores);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(scores);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when STANDARD_ARRAY mode is used with invalid distribution")
    void shouldThrowExceptionWhenStandardArrayIsInvalid() {
        AbilityScores scores = new AbilityScores(18, 16, 14, 12, 10, 8);

        assertThatThrownBy(() -> service.generate(AttributeGenerationMode.STANDARD_ARRAY, scores))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should return valid distribution when MANUAL mode is used with custom scores")
    void shouldGenerateWithManualMode() {
        AbilityScores scores = new AbilityScores(18, 16, 14, 12, 10, 8);

        AbilityScores result = service.generate(AttributeGenerationMode.MANUAL, scores);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(scores);
    }

    @Test
    @DisplayName("Should allow repeated values when MANUAL mode is used")
    void shouldAllowRepeatedValuesInManualMode() {
        AbilityScores scores = new AbilityScores(15, 15, 15, 8, 8, 8);

        AbilityScores result = service.generate(AttributeGenerationMode.MANUAL, scores);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(scores);
    }

    @Test
    @DisplayName("Should throw NullPointerException when generation mode is null")
    void shouldThrowExceptionWhenModeIsNull() {
        AbilityScores scores = new AbilityScores(15, 14, 13, 12, 10, 8);

        assertThatThrownBy(() -> service.generate(null, scores))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Generation mode cannot be null");
    }

    @Test
    @DisplayName("Should throw NullPointerException when AbilityScores is null")
    void shouldThrowExceptionWhenScoresIsNull() {
        assertThatThrownBy(() -> service.generate(AttributeGenerationMode.STANDARD_ARRAY, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Ability scores cannot be null");
    }

}