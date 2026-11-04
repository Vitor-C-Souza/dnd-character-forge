package com.vitorcsouza.app.characterforge.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProficiencyBonusTest {

    @ParameterizedTest
    @CsvSource({
            "1, 2",
            "4, 2",
            "5, 3",
            "8, 3",
            "9, 4",
            "12, 4",
            "13, 5",
            "16, 5",
            "17, 6",
            "20, 6"
    })
    @DisplayName("Should calculate correct proficiency bonus for valid levels 1 to 20")
    void shouldCalculateCorrectProficiencyBonus(int level, int expectedBonus) {
        assertThat(ProficiencyBonus.calculate(level)).isEqualTo(expectedBonus);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 21, 100})
    @DisplayName("Should throw exception when level is outside valid range (1-20)")
    void shouldThrowExceptionWhenLevelInvalid(int invalidLevel) {
        assertThatThrownBy(() -> ProficiencyBonus.calculate(invalidLevel))
                .isInstanceOf(IllegalArgumentException.class);
    }

}