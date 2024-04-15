package io.github.semanticpie.pietunes.recommendation_service.models.dtos;

import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;

import java.util.List;
import java.util.UUID;

public record PlaylistDTO(UUID uuid, String name, PlaylistType type, List<MusicTrackDTO> tracks) {
}
