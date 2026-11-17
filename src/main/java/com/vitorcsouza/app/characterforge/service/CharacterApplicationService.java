package com.vitorcsouza.app.characterforge.service;

import com.vitorcsouza.app.characterforge.domain.model.AbilityScores;
import com.vitorcsouza.app.characterforge.domain.model.Character;
import com.vitorcsouza.app.characterforge.dto.request.AbilityScoresRequest;
import com.vitorcsouza.app.characterforge.dto.request.CreateCharacterRequest;
import com.vitorcsouza.app.characterforge.dto.response.CharacterResponse;
import com.vitorcsouza.app.characterforge.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CharacterApplicationService {

    private static final String CHARACTER_NOT_FOUND_MSG = "Character not found with id: ";

    private final CharacterRepository characterRepository;
    private final CharacterFactory characterFactory;
    private final AttributeGenerationService attributeGenerationService;

    @Transactional
    public CharacterResponse createCharacter(CreateCharacterRequest request) {
        AbilityScores rawScores = toAbilityScores(request.abilityScores());
        AbilityScores validatedScores = attributeGenerationService.generate(
                request.generationMode(),
                rawScores
        );

        Character character = characterFactory.create(
                request.name(),
                request.species(),
                request.characterClass(),
                request.background(),
                validatedScores
        );

        Character saved = characterRepository.save(character);
        return CharacterResponse.fromDomain(saved);
    }

    @Transactional(readOnly = true)
    public List<CharacterResponse> findAllCharacters() {
        return characterRepository.findAll().stream()
                .map(CharacterResponse::fromDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public CharacterResponse findById(UUID id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CHARACTER_NOT_FOUND_MSG + id));
        return CharacterResponse.fromDomain(character);
    }

    @Transactional
    public CharacterResponse updateAbilityScores(UUID id, AbilityScoresRequest request) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CHARACTER_NOT_FOUND_MSG + id));

        character.updateAbilityScores(toAbilityScores(request));
        Character updated = characterRepository.save(character);
        return CharacterResponse.fromDomain(updated);
    }

    @Transactional
    public void softDelete(UUID id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CHARACTER_NOT_FOUND_MSG + id));

        characterRepository.delete(character);
    }

    private AbilityScores toAbilityScores(AbilityScoresRequest dto) {
        return new AbilityScores(
                dto.strength(),
                dto.dexterity(),
                dto.constitution(),
                dto.intelligence(),
                dto.wisdom(),
                dto.charisma()
        );
    }
}