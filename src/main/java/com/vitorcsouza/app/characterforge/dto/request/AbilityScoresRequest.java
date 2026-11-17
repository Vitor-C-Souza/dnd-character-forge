package com.vitorcsouza.app.characterforge.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AbilityScoresRequest(
        @NotNull @Min(1) @Max(30) Integer strength,
        @NotNull @Min(1) @Max(30) Integer dexterity,
        @NotNull @Min(1) @Max(30) Integer constitution,
        @NotNull @Min(1) @Max(30) Integer intelligence,
        @NotNull @Min(1) @Max(30) Integer wisdom,
        @NotNull @Min(1) @Max(30) Integer charisma
) {
}
