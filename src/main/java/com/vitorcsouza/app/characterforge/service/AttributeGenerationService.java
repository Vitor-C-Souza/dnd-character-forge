package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;
import com.vitorcsouza.app.characterforge.service.strategy.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AttributeGenerationService {

    public AbilityScores generate(AttributeGenerationMode mode, AbilityScores scores) {
        Objects.requireNonNull(mode, "Generation mode cannot be null");
        Objects.requireNonNull(scores, "Ability scores cannot be null");

        AttributeGenerationStrategy strategy = switch (mode) {
            case STANDARD_ARRAY -> new StandardArrayStrategy(scores);
            case MANUAL -> new ManualAssignmentStrategy(scores);
            case POINT_BUY -> new PointBuyStrategy(scores);
        };

        return strategy.generate();
    }
}
