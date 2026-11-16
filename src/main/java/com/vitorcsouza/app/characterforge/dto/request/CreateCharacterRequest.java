package com.vitorcsouza.app.characterforge.dto.request;

import com.vitorcsouza.app.characterforge.domain.model.CharacterBackground;
import com.vitorcsouza.app.characterforge.domain.model.CharacterClass;
import com.vitorcsouza.app.characterforge.domain.model.CharacterSpecies;
import com.vitorcsouza.app.characterforge.service.strategy.AttributeGenerationMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCharacterRequest(
        @NotBlank(message = "Character name is required")
        @Size(max = 100, message = "Character name must not exceed 100 characters")
        String name,

        @NotNull(message = "Species is required")
        CharacterSpecies species,

        @NotNull(message = "Character class is required")
        CharacterClass characterClass,

        @NotNull(message = "Background is required")
        CharacterBackground background,

        @NotNull(message = "Generation mode is required")
        AttributeGenerationMode generationMode,

        @NotNull(message = "Ability scores are required")
        @Valid
        AbilityScoresRequest abilityScores
) {
}
