package com.vitorcsouza.app.characterforge.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AbilityScoresTest {

    @Test
    @DisplayName("Deve criar AbilityScores com sucesso quando todos os atributos estiverem no intervalo válido (1 a 30)")
    void shouldCreateAbilityScoresSuccessfully() {
        AbilityScores scores = new AbilityScores(10, 12, 14, 8, 15, 13);

        assertThat(scores.strength()).isEqualTo(10);
        assertThat(scores.dexterity()).isEqualTo(12);
        assertThat(scores.constitution()).isEqualTo(14);
        assertThat(scores.intelligence()).isEqualTo(8);
        assertThat(scores.wisdom()).isEqualTo(15);
        assertThat(scores.charisma()).isEqualTo(13);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 31, 50})
    @DisplayName("Deve lançar IllegalArgumentException quando qualquer atributo for menor que 1 ou maior que 30")
    void shouldThrowExceptionWhenAttributeIsInvalid(int invalidValue) {
        assertThatThrownBy(() -> new AbilityScores(invalidValue, 10, 10, 10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Strength must be between 1 and 30");
    }

}