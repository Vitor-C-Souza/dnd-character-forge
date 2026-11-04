package com.vitorcsouza.app.characterforge.domain.specification;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CharacterSpecificationTest {

    private Character warrior;

    @BeforeEach
    void setUp() {
        AbilityScores scores = new AbilityScores(16, 12, 14, 10, 10, 10);
        warrior = new Character(
                "Conan",
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                scores
        );
    }

    @Test
    @DisplayName("MinimumLevelSpecification should validate level and handle null safely")
    void testMinimumLevelSpecification() {
        Specification<Character> minLevel1 = new MinimumLevelSpecification(1);
        Specification<Character> minLevel5 = new MinimumLevelSpecification(5);

        assertThat(minLevel1.isSatisfiedBy(warrior)).isTrue();
        assertThat(minLevel5.isSatisfiedBy(warrior)).isFalse();
        assertThat(minLevel1.isSatisfiedBy(null)).isFalse();
    }

    @Test
    @DisplayName("MinimumStrengthSpecification should validate strength and handle null safely")
    void testMinimumStrengthSpecification() {
        Specification<Character> minStr15 = new MinimumStrengthSpecification(15);
        Specification<Character> minStr18 = new MinimumStrengthSpecification(18);

        assertThat(minStr15.isSatisfiedBy(warrior)).isTrue();
        assertThat(minStr18.isSatisfiedBy(warrior)).isFalse();
        assertThat(minStr15.isSatisfiedBy(null)).isFalse();
    }

    @Test
    @DisplayName("CharacterClassSpecification should protect against external array mutation")
    void testCharacterClassSpecificationImmutability() {
        CharacterClass[] classes = {CharacterClass.FIGHTER};
        CharacterClassSpecification spec = new CharacterClassSpecification(classes);

        assertThat(spec.isSatisfiedBy(warrior)).isTrue();

        classes[0] = CharacterClass.WIZARD;
        assertThat(spec.isSatisfiedBy(warrior)).isTrue();
        assertThat(spec.isSatisfiedBy(null)).isFalse();
    }

    @Test
    @DisplayName("Combinators (and, or, not) should evaluate true and false scenarios correctly")
    void testSpecificationCombinatorsComprehensive() {
        Specification<Character> level1 = new MinimumLevelSpecification(1);
        Specification<Character> level5 = new MinimumLevelSpecification(5);
        Specification<Character> highStr = new MinimumStrengthSpecification(15);
        Specification<Character> lowStr = new MinimumStrengthSpecification(18); // False para o warrior

        assertThat(level1.and(highStr).isSatisfiedBy(warrior)).isTrue();

        assertThat(level1.and(lowStr).isSatisfiedBy(warrior)).isFalse();

        assertThat(level5.or(highStr).isSatisfiedBy(warrior)).isTrue();

        assertThat(level5.or(lowStr).isSatisfiedBy(warrior)).isFalse();

        assertThat(level5.not().isSatisfiedBy(warrior)).isTrue();

        assertThat(level1.not().isSatisfiedBy(warrior)).isFalse();
    }

    @Test
    @DisplayName("Should throw exception on invalid specification parameters")
    void testInvalidParameters() {
        assertThatThrownBy(() -> new MinimumLevelSpecification(0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MinimumLevelSpecification(21))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MinimumStrengthSpecification(0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MinimumStrengthSpecification(31))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(CharacterClassSpecification::new)
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new CharacterClassSpecification((CharacterClass[]) null))
                .isInstanceOf(NullPointerException.class);
    }
}