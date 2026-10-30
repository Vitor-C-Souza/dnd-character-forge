package com.vitorcsouza.app.characterforge.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tb_characters")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Character extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String species;

    @Column(name = "character_class", nullable = false, length = 30)
    private String characterClass;

    @Column(nullable = false, length = 80)
    private String background;

    @Column(nullable = false)
    private Integer level;

    @Getter
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Integer strength;

    @Getter
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Integer dexterity;

    @Getter
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Integer constitution;

    @Getter
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Integer intelligence;

    @Getter
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Integer wisdom;

    @Getter
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Integer charisma;

    public AbilityScores getAbilityScores() {
        return new AbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public void updateAbilityScores(AbilityScores scores) {
        this.strength = scores.strength();
        this.dexterity = scores.dexterity();
        this.constitution = scores.constitution();
        this.intelligence = scores.intelligence();
        this.wisdom = scores.wisdom();
        this.charisma = scores.charisma();
    }
}
