package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;

public interface AttributeGenerationStrategy {
    AbilityScores generate();
}
