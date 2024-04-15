package io.github.semanticpie.pietunes.recommendation_service.models.dtos;

import java.util.List;
import java.util.UUID;

public record MusicTrackDTO(UUID uuid, String title, String releaseYear, Integer bitrate, Long lengthInMilliseconds, List<String> genres, boolean isLiked, MusicAlbumDTO musicAlbum, MusicBandDTO musicBand) {
}
