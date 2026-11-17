package com.vitorcsouza.app.characterforge.dto.response;

import com.vitorcsouza.app.characterforge.domain.model.Character;
import com.vitorcsouza.app.characterforge.domain.model.CharacterBackground;
import com.vitorcsouza.app.characterforge.domain.model.CharacterClass;
import com.vitorcsouza.app.characterforge.domain.model.CharacterSpecies;

import java.time.Instant;
import java.util.UUID;

public record CharacterResponse(
        UUID id,
        String name,
        CharacterSpecies species,
        CharacterClass characterClass,
        CharacterBackground background,
        Integer level,
        Integer proficiencyBonus,
        AbilityScoresResponse abilityScores,
        Instant createdAt,
        Instant updatedAt
) {
    public static CharacterResponse fromDomain(Character character) {
        return new CharacterResponse(
                character.getId(),
                character.getName(),
                character.getSpecies(),
                character.getCharacterClass(),
                character.getBackground(),
                character.getLevel(),
                character.getProficiencyBonus(),
                new AbilityScoresResponse(
                        character.getStrength(),
                        character.getDexterity(),
                        character.getConstitution(),
                        character.getIntelligence(),
                        character.getWisdom(),
                        character.getCharisma()
                ),
                character.getCreatedAt(),
                character.getUpdatedAt()
        );
    }

    public record AbilityScoresResponse(
            Integer strength,
            Integer dexterity,
            Integer constitution,
            Integer intelligence,
            Integer wisdom,
            Integer charisma
    ) {
    }
}