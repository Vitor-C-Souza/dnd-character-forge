package com.vitorcsouza.app.characterforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorcsouza.app.characterforge.AbstractIntegrationTest;
import com.vitorcsouza.app.characterforge.domain.model.CharacterBackground;
import com.vitorcsouza.app.characterforge.domain.model.CharacterClass;
import com.vitorcsouza.app.characterforge.domain.model.CharacterSpecies;
import com.vitorcsouza.app.characterforge.dto.request.AbilityScoresRequest;
import com.vitorcsouza.app.characterforge.dto.request.CreateCharacterRequest;
import com.vitorcsouza.app.characterforge.service.strategy.AttributeGenerationMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class CharacterApiIT extends AbstractIntegrationTest {

    private static final String BASE = "/api/v1/characters";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldCompleteFullLifecycleWithSoftDeleteAndPersistence() throws Exception {
        String name = "IT-" + UUID.randomUUID();

        CreateCharacterRequest request = new CreateCharacterRequest(
                name,
                CharacterSpecies.HUMAN,
                CharacterClass.PALADIN,
                CharacterBackground.SOLDIER,
                AttributeGenerationMode.STANDARD_ARRAY,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8)
        );

        String response = mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(name))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get(BASE + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.abilityScores.strength").value(15));

        AbilityScoresRequest updateRequest = new AbilityScoresRequest(18, 14, 16, 10, 12, 8);
        mockMvc.perform(patch(BASE + "/{id}/ability-scores", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abilityScores.strength").value(18))
                .andExpect(jsonPath("$.abilityScores.constitution").value(16));

        mockMvc.perform(delete(BASE + "/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", not(hasItem(id))));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tb_characters WHERE name = ? AND deleted_at IS NOT NULL",
                Integer.class,
                name
        );
        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldCreateCharacterWithPointBuyAndRejectInvalidPointBuyEndToEnd() throws Exception {
        String validName = "PB-Valid-" + UUID.randomUUID();

        CreateCharacterRequest validRequest = new CreateCharacterRequest(
                validName,
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                AttributeGenerationMode.POINT_BUY,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8)
        );

        String createdResponse = mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(validName))
                .andExpect(jsonPath("$.abilityScores.strength").value(15))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String createdId = objectMapper.readTree(createdResponse).get("id").asText();

        mockMvc.perform(get(BASE + "/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.name").value(validName))
                .andExpect(jsonPath("$.abilityScores.strength").value(15))
                .andExpect(jsonPath("$.abilityScores.dexterity").value(14))
                .andExpect(jsonPath("$.abilityScores.constitution").value(13))
                .andExpect(jsonPath("$.abilityScores.intelligence").value(12))
                .andExpect(jsonPath("$.abilityScores.wisdom").value(10))
                .andExpect(jsonPath("$.abilityScores.charisma").value(8));

        CreateCharacterRequest overBudgetRequest = new CreateCharacterRequest(
                "PB-OverBudget-" + UUID.randomUUID(),
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                AttributeGenerationMode.POINT_BUY,
                new AbilityScoresRequest(15, 15, 15, 9, 8, 8)
        );

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overBudgetRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Request Argument"));

        CreateCharacterRequest outOfRangeRequest = new CreateCharacterRequest(
                "PB-OutOfRange-" + UUID.randomUUID(),
                CharacterSpecies.HUMAN,
                CharacterClass.FIGHTER,
                CharacterBackground.SOLDIER,
                AttributeGenerationMode.POINT_BUY,
                new AbilityScoresRequest(7, 14, 13, 12, 10, 8)
        );

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outOfRangeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Request Argument"));
    }
}