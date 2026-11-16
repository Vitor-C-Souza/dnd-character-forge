package com.vitorcsouza.app.characterforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorcsouza.app.characterforge.domain.model.CharacterBackground;
import com.vitorcsouza.app.characterforge.domain.model.CharacterClass;
import com.vitorcsouza.app.characterforge.domain.model.CharacterSpecies;
import com.vitorcsouza.app.characterforge.dto.request.AbilityScoresRequest;
import com.vitorcsouza.app.characterforge.dto.request.CreateCharacterRequest;
import com.vitorcsouza.app.characterforge.dto.response.CharacterResponse;
import com.vitorcsouza.app.characterforge.service.CharacterApplicationService;
import com.vitorcsouza.app.characterforge.service.strategy.AttributeGenerationMode;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CharacterController.class)
class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CharacterApplicationService characterApplicationService;

    @Test
    void shouldCreateCharacterSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        CreateCharacterRequest request = new CreateCharacterRequest(
                "Arthas",
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                AttributeGenerationMode.STANDARD_ARRAY,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8)
        );

        CharacterResponse response = new CharacterResponse(
                id,
                "Arthas",
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                1,
                2,
                new CharacterResponse.AbilityScoresResponse(15, 14, 13, 12, 10, 8),
                Instant.now(),
                Instant.now()
        );

        given(characterApplicationService.createCharacter(any(CreateCharacterRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/v1/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Arthas"))
                .andExpect(jsonPath("$.level").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingCharacterWithInvalidData() throws Exception {
        CreateCharacterRequest request = new CreateCharacterRequest(
                "", // Nome vazio viola @NotBlank
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Failed"));
    }

    @Test
    void shouldFindAllCharactersSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        CharacterResponse response = new CharacterResponse(
                id,
                "Arthas",
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                1,
                2,
                new CharacterResponse.AbilityScoresResponse(15, 14, 13, 12, 10, 8),
                Instant.now(),
                Instant.now()
        );

        given(characterApplicationService.findAllCharacters()).willReturn(List.of(response));

        mockMvc.perform(get("/api/v1/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Arthas"));
    }

    @Test
    void shouldFindCharacterByIdSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        CharacterResponse response = new CharacterResponse(
                id,
                "Arthas",
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                1,
                2,
                new CharacterResponse.AbilityScoresResponse(15, 14, 13, 12, 10, 8),
                Instant.now(),
                Instant.now()
        );

        given(characterApplicationService.findById(id)).willReturn(response);

        mockMvc.perform(get("/api/v1/characters/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Arthas"));
    }

    @Test
    void shouldReturnNotFoundWhenCharacterDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        given(characterApplicationService.findById(id))
                .willThrow(new EntityNotFoundException("Character not found with id: " + id));

        mockMvc.perform(get("/api/v1/characters/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"));
    }

    @Test
    void shouldUpdateAbilityScoresSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        AbilityScoresRequest request = new AbilityScoresRequest(16, 14, 13, 12, 10, 8);
        CharacterResponse response = new CharacterResponse(
                id,
                "Arthas",
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                1,
                2,
                new CharacterResponse.AbilityScoresResponse(16, 14, 13, 12, 10, 8),
                Instant.now(),
                Instant.now()
        );

        given(characterApplicationService.updateAbilityScores(eq(id), any(AbilityScoresRequest.class))).willReturn(response);

        mockMvc.perform(patch("/api/v1/characters/{id}/ability-scores", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.abilityScores.strength").value(16));
    }

    @Test
    void shouldReturnConflictWhenOptimisticLockingFails() throws Exception {
        UUID id = UUID.randomUUID();
        AbilityScoresRequest request = new AbilityScoresRequest(16, 14, 13, 12, 10, 8);

        given(characterApplicationService.updateAbilityScores(eq(id), any(AbilityScoresRequest.class)))
                .willThrow(new OptimisticLockingFailureException("Optimistic lock conflict"));

        mockMvc.perform(patch("/api/v1/characters/{id}/ability-scores", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Resource Conflict"));
    }

    @Test
    void shouldDeleteCharacterSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/characters/{id}", id))
                .andExpect(status().isNoContent());
    }
}