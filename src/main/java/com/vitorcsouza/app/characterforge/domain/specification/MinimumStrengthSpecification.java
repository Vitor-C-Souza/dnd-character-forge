package com.vitorcsouza.app.characterforge.domain.specification;

import com.vitorcsouza.app.characterforge.domain.model.Character;

public record MinimumStrengthSpecification(int minimumStrength) implements Specification<Character> {

    public MinimumStrengthSpecification {
        if (minimumStrength < 1 || minimumStrength > 30) {
            throw new IllegalArgumentException(
                    "Minimum strength must be between 1 and 30"
            );
        }
    }

    @Override
    public boolean isSatisfiedBy(Character character) {
        return character != null
                && character.getAbilityScores().strength()
                >= minimumStrength;
    }
}
