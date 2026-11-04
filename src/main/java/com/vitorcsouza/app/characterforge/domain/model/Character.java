package com.vitorcsouza.app.characterforge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "tb_characters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Character extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CharacterSpecies species;

    @Enumerated(EnumType.STRING)
    @Column(name = "character_class", nullable = false, length = 30)
    private CharacterClass characterClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 80)
    private CharacterBackground background;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer strength;

    @Column(nullable = false)
    private Integer dexterity;

    @Column(nullable = false)
    private Integer constitution;

    @Column(nullable = false)
    private Integer intelligence;

    @Column(nullable = false)
    private Integer wisdom;

    @Column(nullable = false)
    private Integer charisma;

    public Character(
            String name,
            CharacterSpecies species,
            CharacterClass characterClass,
            CharacterBackground background,
            AbilityScores scores
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Character name cannot be null or blank"
            );
        }

        String trimmedName = name.trim();

        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException(
                    "Character name cannot exceed 100 characters"
            );
        }

        this.name = trimmedName;

        this.species = Objects.requireNonNull(
                species, "Character species cannot be null"
        );

        this.characterClass = Objects.requireNonNull(
                characterClass, "Character class cannot be null"
        );

        this.background = Objects.requireNonNull(
                background, "Character background cannot be null"
        );

        this.level = 1;

        updateAbilityScores(scores);
    }

    public AbilityScores getAbilityScores() {
        return new AbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public void updateAbilityScores(AbilityScores scores) {
        Objects.requireNonNull(scores, "AbilityScores cannot be null");

        this.strength = scores.strength();
        this.dexterity = scores.dexterity();
        this.constitution = scores.constitution();
        this.intelligence = scores.intelligence();
        this.wisdom = scores.wisdom();
        this.charisma = scores.charisma();
    }
}
