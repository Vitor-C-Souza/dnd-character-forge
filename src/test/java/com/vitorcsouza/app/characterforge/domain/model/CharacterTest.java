package com.vitorcsouza.app.characterforge.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CharacterTest {

    @Test
    @DisplayName("Should return proficiency bonus 2 for a newly created level 1 character")
    void shouldReturnProficiencyBonusForLevel1Character() {
        AbilityScores dummyScores = new AbilityScores(10, 10, 10, 10, 10, 10);

        Character character = new Character(
                "Hero",
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                dummyScores
        );

        assertThat(character.getLevel()).isEqualTo(1);
        assertThat(character.getProficiencyBonus()).isEqualTo(2);
    }
}