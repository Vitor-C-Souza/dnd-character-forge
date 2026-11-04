package com.vitorcsouza.app.characterforge.domain.model;

public final class ProficiencyBonus {
    private ProficiencyBonus() {
    }

    public static int calculate(int level) {
        if (level < 1 || level > 20) {
            throw new IllegalArgumentException(
                    "Character level must be between 1 and 20"
            );
        }

        return 2 + (level - 1) / 4;
    }
}
