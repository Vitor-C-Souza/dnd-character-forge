package com.vitorcsouza.app.characterforge.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CharacterTest {

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
    @DisplayName("Should calculate correct proficiency bonus based on character level")
    void shouldCalculateCorrectProficiencyBonus(int level, int expectedProficiencyBonus) {
        AbilityScores dummyScores = new AbilityScores(10, 10, 10, 10, 10, 10);

        Character character = new Character(
                "Hero",
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                level,
                dummyScores
        );

        assertThat(character.getProficiencyBonus()).isEqualTo(expectedProficiencyBonus);
    }
}