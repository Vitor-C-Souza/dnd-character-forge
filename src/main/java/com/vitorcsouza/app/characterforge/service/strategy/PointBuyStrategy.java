package com.vitorcsouza.app.characterforge.service.strategy;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PointBuyStrategy implements AttributeGenerationStrategy {
    public static final int POINT_BUY_BUDGET = 27;
    public static final int MIN_SCORE = 8;
    public static final int MAX_SCORE = 15;

    private static final Map<Integer, Integer> SCORE_COSTS = Map.of(
            8, 0,
            9, 1,
            10, 2,
            11, 3,
            12, 4,
            13, 5,
            14, 7,
            15, 9
    );

    private final AbilityScores scores;

    public PointBuyStrategy(AbilityScores scores) {
        Objects.requireNonNull(scores, "AbilityScores cannot be null");
        validatePointBuy(scores);
        this.scores = scores;
    }

    @Override
    public AbilityScores generate() {
        return scores;
    }

    private void validatePointBuy(AbilityScores scores) {
        List<Integer> abilityScores = List.of(
                scores.strength(),
                scores.dexterity(),
                scores.constitution(),
                scores.intelligence(),
                scores.wisdom(),
                scores.charisma()
        );

        int totalCost = 0;
        for (int score : abilityScores) {
            if (score < MIN_SCORE || score > MAX_SCORE) {
                throw new IllegalArgumentException(
                        String.format(
                                "Invalid Point Buy ability score %d. Scores must be between %d and %d.",
                                score, MIN_SCORE, MAX_SCORE
                        )
                );
            }
            totalCost += SCORE_COSTS.get(score);
        }

        if (totalCost > POINT_BUY_BUDGET) {
            throw new IllegalArgumentException(
                    String.format(
                            "Point Buy budget exceeded: cost is %d, maximum allowed is %d.",
                            totalCost, POINT_BUY_BUDGET
                    )
            );
        }
    }
}
