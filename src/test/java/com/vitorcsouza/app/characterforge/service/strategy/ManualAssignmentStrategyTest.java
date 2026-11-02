package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ManualAssignmentStrategyTest {

    @Test
    @DisplayName("Should return the exact AbilityScores provided in constructor")
    void shouldReturnProvidedAbilityScores() {

        AbilityScores expectedScores = new AbilityScores(18, 14, 12, 10, 10, 8);
        ManualAssignmentStrategy strategy = new ManualAssignmentStrategy(expectedScores);


        AbilityScores actualScores = strategy.generate();


        assertThat(actualScores)
                .isNotNull()
                .isEqualTo(expectedScores);
        assertThat(actualScores.strength()).isEqualTo(18);
        assertThat(actualScores.dexterity()).isEqualTo(14);
    }

    @Test
    @DisplayName("Should work correctly when consumed through the AttributeGenerationStrategy interface contract")
    void shouldWorkThroughInterfaceContract() {

        AttributeGenerationStrategy strategy = new ManualAssignmentStrategy(
                new AbilityScores(15, 15, 15, 8, 8, 8)
        );


        AbilityScores result = strategy.generate();


        assertThat(result).isNotNull();
        assertThat(result.strength()).isEqualTo(15);
        assertThat(result.charisma()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should throw NullPointerException when initialized with null AbilityScores")
    void shouldThrowExceptionWhenScoresIsNull() {
        assertThatThrownBy(() -> new ManualAssignmentStrategy(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("cannot be null");
    }

}