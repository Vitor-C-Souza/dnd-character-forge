package com.vitorcsouza.app.characterforge.repository;

import com.vitorcsouza.app.characterforge.AbstractIntegrationTest;
import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
class CharacterRepositoryIT extends AbstractIntegrationTest {
    @Autowired
    private CharacterRepository characterRepository;

    @Test
    void shouldPersistAndRetrieveCharacterOnRealMySQL() {
        // Arrange
        AbilityScores scores = new AbilityScores(16, 14, 15, 10, 12, 8);
        Character character = new Character(
                "Thorin Escudo de Carvalho",
                CharacterSpecies.DWARF,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                scores
        );

        Character saved = characterRepository.saveAndFlush(character);

        Character found = characterRepository.findById(saved.getId())
                .orElseThrow(() -> new AssertionError("Character should be present"));

        // Assert
        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("Thorin Escudo de Carvalho");
        assertThat(found.getLevel()).isEqualTo(1);
        assertThat(found.getStrength()).isEqualTo(16);
        assertThat(found.getCharisma()).isEqualTo(8);
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getUpdatedAt()).isNotNull();
        assertThat(found.getVersion()).isZero();

        Instant initialUpdatedAt = found.getUpdatedAt();
        found.updateAbilityScores(new AbilityScores(18, 14, 15, 10, 12, 8));
        Character updated = characterRepository.saveAndFlush(found);

        assertThat(updated.getStrength()).isEqualTo(18);
        assertThat(updated.getVersion()).isEqualTo(1L);
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(initialUpdatedAt);
    }
}