package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.*;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import com.vitorcsouza.app.characterforge.dto.request.AbilityScoresRequest;
import com.vitorcsouza.app.characterforge.dto.request.CreateCharacterRequest;
import com.vitorcsouza.app.characterforge.dto.response.CharacterResponse;
import com.vitorcsouza.app.characterforge.repository.CharacterRepository;
import com.vitorcsouza.app.characterforge.service.strategy.AttributeGenerationMode;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CharacterApplicationServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CharacterFactory characterFactory;

    @Mock
    private AttributeGenerationService attributeGenerationService;

    @InjectMocks
    private CharacterApplicationService characterApplicationService;

    @Test
    void shouldCreateCharacterSuccessfully() {
        CreateCharacterRequest request = new CreateCharacterRequest(
                "Arthas",
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                AttributeGenerationMode.STANDARD_ARRAY,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8)
        );

        AbilityScores scores = new AbilityScores(15, 14, 13, 12, 10, 8);
        Character character = new Character("Arthas", CharacterSpecies.HUMAN, CharacterClass.PALADIN, CharacterBackground.SOLDIER, scores);

        given(attributeGenerationService.generate(any(), any())).willReturn(scores);
        given(characterFactory.create(any(), any(), any(), any(), any())).willReturn(character);
        given(characterRepository.save(any(Character.class))).willReturn(character);

        CharacterResponse response = characterApplicationService.createCharacter(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Arthas");
        verify(characterRepository).save(any(Character.class));
    }

    @Test
    void shouldFindCharacterByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        AbilityScores scores = new AbilityScores(15, 14, 13, 12, 10, 8);
        Character character = new Character("Arthas", CharacterSpecies.HUMAN, CharacterClass.PALADIN, CharacterBackground.SOLDIER, scores);

        given(characterRepository.findById(id)).willReturn(Optional.of(character));

        CharacterResponse response = characterApplicationService.findById(id);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Arthas");
    }

    @Test
    void shouldThrowEntityNotFoundWhenCharacterNotFound() {
        UUID id = UUID.randomUUID();
        given(characterRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> characterApplicationService.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Character not found with id: " + id);
    }

    @Test
    void shouldSoftDeleteSuccessfully() {
        UUID id = UUID.randomUUID();
        AbilityScores scores = new AbilityScores(15, 14, 13, 12, 10, 8);
        Character character = new Character("Arthas", CharacterSpecies.HUMAN, CharacterClass.PALADIN, CharacterBackground.SOLDIER, scores);

        given(characterRepository.findById(id)).willReturn(Optional.of(character));

        characterApplicationService.softDelete(id);

        verify(characterRepository).delete(character);
    }
}