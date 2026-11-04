package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CharacterFactoryTest {
    private CharacterFactory characterFactory;
    private AbilityScores defaultScores;

    @BeforeEach
    void setUp() {
        characterFactory = new CharacterFactory();
        defaultScores = new AbilityScores(15, 14, 13, 12, 10, 8);
    }

    @Test
    @DisplayName("Should successfully create a character with valid parameters and level 1")
    void shouldCreateCharacterSuccessfully() {
        Character character = characterFactory.create(
                "Thorin",
                CharacterSpecies.DWARF,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                defaultScores
        );

        assertThat(character).isNotNull();
        assertThat(character.getName()).isEqualTo("Thorin");
        assertThat(character.getSpecies()).isEqualTo(CharacterSpecies.DWARF);
        assertThat(character.getCharacterClass()).isEqualTo(CharacterClass.FIGHTER);
        assertThat(character.getBackground()).isEqualTo(CharacterBackground.SOLDIER);
        assertThat(character.getLevel()).isEqualTo(1);
        assertThat(character.getAbilityScores()).isEqualTo(defaultScores);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when character name is null, empty or blank")
    void shouldThrowExceptionWhenNameInvalid(String invalidName) {
        assertThatThrownBy(() -> characterFactory.create(
                invalidName,
                CharacterSpecies.DWARF,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                defaultScores
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw exception when character name exceeds 100 characters")
    void shouldThrowExceptionWhenNameTooLong() {
        String longName = "A".repeat(101);
        assertThatThrownBy(() -> characterFactory.create(
                longName,
                CharacterSpecies.DWARF,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                defaultScores
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw exception when mandatory enums or ability scores are null")
    void shouldThrowExceptionWhenMandatoryFieldsNull() {
        assertThatThrownBy(() -> characterFactory.create("Thorin", null, CharacterClass.FIGHTER, CharacterBackground.SOLDIER, defaultScores))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> characterFactory.create("Thorin", CharacterSpecies.DWARF, null, CharacterBackground.SOLDIER, defaultScores))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> characterFactory.create("Thorin", CharacterSpecies.DWARF, CharacterClass.FIGHTER, null, defaultScores))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> characterFactory.create("Thorin", CharacterSpecies.DWARF, CharacterClass.FIGHTER, CharacterBackground.SOLDIER, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should allow multiple characters to share the same name")
    void shouldAllowSameNameToMultipleCharacters() {
        Character first = characterFactory.create("Arthas", CharacterSpecies.HUMAN, CharacterClass.PALADIN, CharacterBackground.NOBLE, defaultScores);
        Character second = characterFactory.create("Arthas", CharacterSpecies.ELF, CharacterClass.WIZARD, CharacterBackground.SAGE, defaultScores);

        assertThat(first.getName()).isEqualTo(second.getName());
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("Should reject invalid character created directly")
    void shouldRejectInvalidCharacterCreatedDirectly() {
        AbilityScores scores =
                new AbilityScores(15, 14, 13, 12, 10, 8);

        assertThatThrownBy(() -> new Character(
                "",
                CharacterSpecies.ELF,
                CharacterClass.WIZARD,
                CharacterBackground.SAGE,
                scores
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should create character at level one")
    void shouldCreateCharacterAtLevelOne() {
        AbilityScores scores =
                new AbilityScores(15, 14, 13, 12, 10, 8);

        Character character = new Character(
                "Gandalf",
                CharacterSpecies.HUMAN,
                CharacterClass.WIZARD,
                CharacterBackground.SAGE,
                scores
        );

        assertThat(character.getLevel()).isEqualTo(1);
    }
}