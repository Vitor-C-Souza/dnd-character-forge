package com.vitorcsouza.app.characterforge.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbilityScoresTest {

    @Test
    @DisplayName("Deve criar AbilityScores com sucesso quando todos os atributos forem válidos")
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
    @ValueSource(ints = {1, 30})
    @DisplayName("Deve aceitar valores exatos dos limites mínimos (1) e máximos (30)")
    void shouldAcceptBoundaryValues(int boundaryValue) {
        AbilityScores scores = new AbilityScores(
                boundaryValue, boundaryValue, boundaryValue,
                boundaryValue, boundaryValue, boundaryValue
        );

        assertThat(scores.strength()).isEqualTo(boundaryValue);
        assertThat(scores.charisma()).isEqualTo(boundaryValue);
    }

    @Test
    @DisplayName("Deve rejeitar valor inválido em Força (Strength)")
    void shouldThrowExceptionWhenStrengthIsInvalid() {
        assertThatThrownBy(() -> new AbilityScores(0, 10, 10, 10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Strength");
    }

    @Test
    @DisplayName("Deve rejeitar valor inválido em Destreza (Dexterity)")
    void shouldThrowExceptionWhenDexterityIsInvalid() {
        assertThatThrownBy(() -> new AbilityScores(10, 31, 10, 10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dexterity");
    }

    @Test
    @DisplayName("Deve rejeitar valor inválido em Constituição (Constitution)")
    void shouldThrowExceptionWhenConstitutionIsInvalid() {
        assertThatThrownBy(() -> new AbilityScores(10, 10, -1, 10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Constitution");
    }

    @Test
    @DisplayName("Deve rejeitar valor inválido em Inteligência (Intelligence)")
    void shouldThrowExceptionWhenIntelligenceIsInvalid() {
        assertThatThrownBy(() -> new AbilityScores(10, 10, 10, 35, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Intelligence");
    }

    @Test
    @DisplayName("Deve rejeitar valor inválido em Sabedoria (Wisdom)")
    void shouldThrowExceptionWhenWisdomIsInvalid() {
        assertThatThrownBy(() -> new AbilityScores(10, 10, 10, 10, 0, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wisdom");
    }

    @Test
    @DisplayName("Deve rejeitar valor inválido em Carisma (Charisma)")
    void shouldThrowExceptionWhenCharismaIsInvalid() {
        assertThatThrownBy(() -> new AbilityScores(10, 10, 10, 10, 10, 31))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Charisma");
    }

    @Test
    @DisplayName("AbilityScores com os mesmos valores devem ser iguais e possuir o mesmo hashCode")
    void shouldBeEqualWhenScoresAreEqual() {
        AbilityScores first = new AbilityScores(15, 14, 13, 12, 10, 8);
        AbilityScores second = new AbilityScores(15, 14, 13, 12, 10, 8);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("AbilityScores com valores diferentes devem ser diferentes")
    void shouldNotBeEqualWhenScoresAreDifferent() {
        AbilityScores first = new AbilityScores(15, 14, 13, 12, 10, 8);
        AbilityScores second = new AbilityScores(16, 14, 13, 12, 10, 8);

        assertThat(first).isNotEqualTo(second);
    }
}