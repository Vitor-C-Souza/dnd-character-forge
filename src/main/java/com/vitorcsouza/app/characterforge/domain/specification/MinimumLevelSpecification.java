package com.vitorcsouza.app.characterforge.domain.specification;

import com.vitorcsouza.app.characterforge.domain.model.Character;

public record MinimumLevelSpecification(int minimumLevel) implements Specification<Character> {

    public MinimumLevelSpecification {
        if (minimumLevel < 1 || minimumLevel > 20) {
            throw new IllegalArgumentException(
                    "Minimum level must be between 1 and 20"
            );
        }
    }

    @Override
    public boolean isSatisfiedBy(Character character) {
        return character != null
                && character.getLevel() >= minimumLevel;
    }
}
