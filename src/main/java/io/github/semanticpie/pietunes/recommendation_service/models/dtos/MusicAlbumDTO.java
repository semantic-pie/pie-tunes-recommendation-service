package io.github.semanticpie.pietunes.recommendation_service.models.dtos;

import java.util.UUID;

public record MusicAlbumDTO(UUID uuid, String name, String description, int yearOfRecord) {
}
