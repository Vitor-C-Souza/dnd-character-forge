package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterFactory {

    public Character create(String name, CharacterSpecies species, CharacterClass characterClass, CharacterBackground background, AbilityScores scores) {

        return new Character(name, species, characterClass, background, scores);
    }
}
