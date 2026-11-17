package com.vitorcsouza.app.characterforge.repository;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CharacterRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should save and retrieve character with UUID, auditing, and versioning successfully")
    void shouldSaveAndRetrieveCharacter() {
        AbilityScores scores = new AbilityScores(16, 12, 14, 10, 10, 10);
        Character character = new Character(
                "Aragorn",
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                scores
        );

        Character savedCharacter = characterRepository.save(character);
        entityManager.flush();
        entityManager.clear();

        UUID characterId = savedCharacter.getId();
        Character foundCharacter = characterRepository.findById(characterId).orElseThrow();

        assertThat(foundCharacter.getName()).isEqualTo("Aragorn");
        assertThat(foundCharacter.getSpecies()).isEqualTo(CharacterSpecies.HUMAN);
        assertThat(foundCharacter.getCharacterClass()).isEqualTo(CharacterClass.FIGHTER);
        assertThat(foundCharacter.getCreatedAt()).isNotNull();
        assertThat(foundCharacter.getUpdatedAt()).isNotNull();
        assertThat(foundCharacter.getVersion()).isZero();
    }
}