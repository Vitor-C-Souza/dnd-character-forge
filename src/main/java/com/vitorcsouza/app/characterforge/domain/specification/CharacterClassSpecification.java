package com.vitorcsouza.app.characterforge.domain.specification;

import com.vitorcsouza.app.characterforge.domain.model.Character;
import com.vitorcsouza.app.characterforge.domain.model.CharacterClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CharacterClassSpecification implements Specification<Character> {

    private final List<CharacterClass> allowedClasses;

    public CharacterClassSpecification(CharacterClass... allowedClasses) {
        Objects.requireNonNull(allowedClasses, "Allowed classes cannot be null");
        if (allowedClasses.length == 0) {
            throw new IllegalArgumentException("At least one allowed class must be provided");
        }
        this.allowedClasses = List.copyOf(Arrays.asList(allowedClasses));
    }

    @Override
    public boolean isSatisfiedBy(Character character) {
        return character != null
                && allowedClasses.contains(character.getCharacterClass());
    }
}
