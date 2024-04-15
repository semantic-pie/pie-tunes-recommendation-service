package io.github.semanticpie.pietunes.recommendation_service.models.dtos;

import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;

import java.util.UUID;

public record NestedPlaylistDTO(UUID uuid, String name, PlaylistType type) {
}
