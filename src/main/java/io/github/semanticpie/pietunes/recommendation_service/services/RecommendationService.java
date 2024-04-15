package io.github.semanticpie.pietunes.recommendation_service.services;


import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.Playlist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RecommendationService {

    Mono<Void> generatePlaylists();

    Mono<Playlist> findPlaylistById(UUID id);

    Flux<Playlist> findPlaylistsAndSortByDate(UUID personId, String type);
}
