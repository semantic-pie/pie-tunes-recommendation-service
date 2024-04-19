package io.github.semanticpie.pietunes.recommendation_service.models.dtos;

import java.util.List;
import java.util.UUID;

public record UpdatePlaylistDTO(UUID uuid, String name, List<UUID> tracks) {
}
