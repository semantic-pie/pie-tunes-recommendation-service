package io.github.semanticpie.pietunes.recommendation_service.models.dtos;

import java.util.UUID;

public record MusicBandDTO(UUID uuid, String name, String description) {
}
