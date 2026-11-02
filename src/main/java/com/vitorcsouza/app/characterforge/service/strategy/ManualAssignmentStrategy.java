package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;

import java.util.Objects;

public class ManualAssignmentStrategy implements AttributeGenerationStrategy {

    private final AbilityScores abilityScores;

    public ManualAssignmentStrategy(AbilityScores abilityScores) {
        this.abilityScores = Objects.requireNonNull(abilityScores, "Ability scores cannot be null");
    }

    @Override
    public AbilityScores generate() {
        return this.abilityScores;
    }
}
