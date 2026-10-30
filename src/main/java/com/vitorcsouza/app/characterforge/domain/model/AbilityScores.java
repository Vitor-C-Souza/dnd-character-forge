package com.vitorcsouza.app.characterforge.domain.model;

public record AbilityScores(
        int strength,
        int dexterity,
        int constitution,
        int intelligence,
        int wisdom,
        int charisma
) {
    public AbilityScores {
        validateScore(strength, "Strength");
        validateScore(dexterity, "Dexterity");
        validateScore(constitution, "Constitution");
        validateScore(intelligence, "Intelligence");
        validateScore(wisdom, "Wisdom");
        validateScore(charisma, "Charisma");
    }

    private void validateScore(int score, String attributeName) {
        if (score < 1 || score > 30) {
            throw new IllegalArgumentException(
                    String.format("Attribute %s must be between 1 and 30. Provided: %d", attributeName, score)
            );
        }
    }
}
