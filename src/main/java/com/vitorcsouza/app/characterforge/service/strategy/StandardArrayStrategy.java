package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class StandardArrayStrategy implements AttributeGenerationStrategy {

    private static final List<Integer> STANDARD_ARRAY_SORTED = List.of(8, 10, 12, 13, 14, 15);

    private final AbilityScores scores;

    public StandardArrayStrategy(AbilityScores scores) {
        Objects.requireNonNull(scores, "AbilityScores cannot be null");
        validateStandardArray(scores);
        this.scores = scores;
    }

    @Override
    public AbilityScores generate() {
        return scores;
    }

    private void validateStandardArray(AbilityScores scores) {
        List<Integer> providedScoresSorted = Stream.of(
                scores.strength(),
                scores.dexterity(),
                scores.constitution(),
                scores.intelligence(),
                scores.wisdom(),
                scores.charisma()
        ).sorted().toList();

        if (!STANDARD_ARRAY_SORTED.equals(providedScoresSorted)) {
            throw new IllegalArgumentException(
                    "Invalid Standard Array distribution. Must contain exactly values: 15, 14, 13, 12, 10, 8."
            );
        }
    }
}
