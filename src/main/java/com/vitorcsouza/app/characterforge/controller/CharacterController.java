package com.vitorcsouza.app.characterforge.controller;

import com.vitorcsouza.app.characterforge.dto.request.AbilityScoresRequest;
import com.vitorcsouza.app.characterforge.dto.request.CreateCharacterRequest;
import com.vitorcsouza.app.characterforge.dto.response.CharacterResponse;
import com.vitorcsouza.app.characterforge.service.CharacterApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/characters")
@RequiredArgsConstructor
@Tag(name = "Characters", description = "Endpoints for managing D&D character sheets")
public class CharacterController {

    private final CharacterApplicationService service;

    @PostMapping
    @Operation(summary = "Create a new character", description = "Creates and validates a D&D character sheet using STANDARD_ARRAY, MANUAL, or POINT_BUY attribute generation.")
    @ApiResponse(responseCode = "201", description = "Character created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data or domain rule violation")
    public ResponseEntity<CharacterResponse> create(
            @RequestBody @Valid CreateCharacterRequest request,
            UriComponentsBuilder builder
    ) {
        CharacterResponse response = service.createCharacter(request);
        URI location = builder.path("/api/v1/characters/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "List all active characters", description = "Returns all non-deleted characters.")
    @ApiResponse(responseCode = "200", description = "List of characters retrieved successfully")
    public ResponseEntity<List<CharacterResponse>> findAll() {
        return ResponseEntity.ok(service.findAllCharacters());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find character by ID", description = "Retrieves a single active character by its UUID.")
    @ApiResponse(responseCode = "200", description = "Character found")
    @ApiResponse(responseCode = "404", description = "Character not found or deleted")
    public ResponseEntity<CharacterResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}/ability-scores")
    @Operation(summary = "Update character ability scores", description = "Updates the six core D&D ability scores for an existing character.")
    @ApiResponse(responseCode = "200", description = "Ability scores updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ability score values")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<CharacterResponse> updateAbilityScores(
            @PathVariable UUID id,
            @RequestBody @Valid AbilityScoresRequest request
    ) {
        return ResponseEntity.ok(service.updateAbilityScores(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a character", description = "Marks the character as deleted without physically removing the record from the database.")
    @ApiResponse(responseCode = "204", description = "Character deleted successfully")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}