package com.vitorcsouza.app.characterforge.domain.repository;

import com.vitorcsouza.app.characterforge.domain.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CharacterRepository extends JpaRepository<Character, UUID> {
}
