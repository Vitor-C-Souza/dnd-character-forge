package com.vitorcsouza.app.characterforge.repository;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CharacterRepositoryIT {
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

        // Act
        Character saved = characterRepository.saveAndFlush(character);
        Optional<Character> retrieved = characterRepository.findById(saved.getId());

        // Assert
        assertThat(retrieved).isPresent();
        Character found = retrieved.get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("Thorin Escudo de Carvalho");
        assertThat(found.getLevel()).isEqualTo(1);
        assertThat(found.getStrength()).isEqualTo(16);
        assertThat(found.getCharisma()).isEqualTo(8);
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getUpdatedAt()).isNotNull();
        assertThat(found.getVersion()).isEqualTo(0L);

        // Act - Update para validar @Version e @UpdateTimestamp
        Instant initialUpdatedAt = found.getUpdatedAt();
        found.updateAbilityScores(new AbilityScores(18, 14, 15, 10, 12, 8));
        Character updated = characterRepository.saveAndFlush(found);

        // Assert - Optimistic Locking & Auditing
        assertThat(updated.getStrength()).isEqualTo(18);
        assertThat(updated.getVersion()).isEqualTo(1L);
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(initialUpdatedAt);
    }
}
