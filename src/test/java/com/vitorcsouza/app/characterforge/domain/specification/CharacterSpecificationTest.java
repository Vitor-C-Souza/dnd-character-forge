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
    @DisplayName("MinimumLevelSpecification should validate character level correctly")
    void testMinimumLevelSpecification() {
        Specification<Character> minLevel1 = new MinimumLevelSpecification(1);
        Specification<Character> minLevel5 = new MinimumLevelSpecification(5);

        assertThat(minLevel1.isSatisfiedBy(warrior)).isTrue();
        assertThat(minLevel5.isSatisfiedBy(warrior)).isFalse();
    }

    @Test
    @DisplayName("MinimumStrengthSpecification should validate character strength correctly")
    void testMinimumStrengthSpecification() {
        Specification<Character> minStr15 = new MinimumStrengthSpecification(15);
        Specification<Character> minStr18 = new MinimumStrengthSpecification(18);

        assertThat(minStr15.isSatisfiedBy(warrior)).isTrue();
        assertThat(minStr18.isSatisfiedBy(warrior)).isFalse();
    }

    @Test
    @DisplayName("CharacterClassSpecification should validate allowed classes correctly")
    void testCharacterClassSpecification() {
        Specification<Character> fighterOrPaladin = new CharacterClassSpecification(
                CharacterClass.FIGHTER, CharacterClass.PALADIN
        );
        Specification<Character> wizardOnly = new CharacterClassSpecification(
                CharacterClass.WIZARD
        );

        assertThat(fighterOrPaladin.isSatisfiedBy(warrior)).isTrue();
        assertThat(wizardOnly.isSatisfiedBy(warrior)).isFalse();
    }

    @Test
    @DisplayName("Combinators (and, or, not) should compose specifications correctly")
    void testSpecificationCombinators() {
        Specification<Character> level1 = new MinimumLevelSpecification(1);
        Specification<Character> level5 = new MinimumLevelSpecification(5);
        Specification<Character> highStrength = new MinimumStrengthSpecification(15);
        Specification<Character> wizardClass = new CharacterClassSpecification(CharacterClass.WIZARD);

        Specification<Character> eligibleHero = level1.and(highStrength);
        assertThat(eligibleHero.isSatisfiedBy(warrior)).isTrue();

        Specification<Character> veteranHero = level5.and(highStrength);
        assertThat(veteranHero.isSatisfiedBy(warrior)).isFalse();

        Specification<Character> flexibleHero = level5.or(highStrength);
        assertThat(flexibleHero.isSatisfiedBy(warrior)).isTrue();

        Specification<Character> notWizard = wizardClass.not();
        assertThat(notWizard.isSatisfiedBy(warrior)).isTrue();
    }

    @Test
    @DisplayName("Should throw exception on invalid specification parameters")
    void testInvalidParameters() {
        assertThatThrownBy(() -> new MinimumLevelSpecification(25))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MinimumStrengthSpecification(0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(CharacterClassSpecification::new)
                .isInstanceOf(IllegalArgumentException.class);
    }
}