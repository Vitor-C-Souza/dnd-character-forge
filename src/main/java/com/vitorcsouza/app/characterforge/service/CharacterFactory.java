package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CharacterFactory {

    public Character create(
            String name,
            CharacterSpecies species,
            CharacterClass characterClass,
            CharacterBackground background,
            AbilityScores scores
    ) {
        validateName(name);
        Objects.requireNonNull(species, "Character species cannot be null");
        Objects.requireNonNull(characterClass, "Character class cannot be null");
        Objects.requireNonNull(background, "Character background cannot be null");
        Objects.requireNonNull(scores, "Ability scores cannot be null");

        return new Character(
                name.trim(),
                species,
                characterClass,
                background,
                1,
                scores
        );
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Character name cannot be null");
        }
        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be empty or blank");
        }
        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("Character name cannot exceed 100 characters");
        }
    }
}
