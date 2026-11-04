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

    private static void validateScore(int score, String attributeName) {
        if (score < 1 || score > 30) {
            throw new IllegalArgumentException(
                    String.format("Attribute %s must be between 1 and 30. Provided: %d", attributeName, score)
            );
        }
    }

    private static int calculateModifier(int score) {
        return Math.floorDiv(score - 10, 2);
    }

    public int strengthModifier() {
        return calculateModifier(strength);
    }

    public int dexterityModifier() {
        return calculateModifier(dexterity);
    }

    public int constitutionModifier() {
        return calculateModifier(constitution);
    }

    public int intelligenceModifier() {
        return calculateModifier(intelligence);
    }

    public int wisdomModifier() {
        return calculateModifier(wisdom);
    }

    public int charismaModifier() {
        return calculateModifier(charisma);
    }
}
