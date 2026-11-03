package com.vitorcsouza.app.characterforge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "tb_characters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public Character(String name, CharacterSpecies species, CharacterClass characterClass,
                     CharacterBackground background, Integer level, AbilityScores scores) {
        this.name = name;
        this.species = species;
        this.characterClass = characterClass;
        this.background = background;
        this.level = level;
        this.strength = scores.strength();
        this.dexterity = scores.dexterity();
        this.constitution = scores.constitution();
        this.intelligence = scores.intelligence();
        this.wisdom = scores.wisdom();
        this.charisma = scores.charisma();
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
