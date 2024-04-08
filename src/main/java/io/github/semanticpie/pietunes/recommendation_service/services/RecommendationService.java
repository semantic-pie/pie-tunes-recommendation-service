package io.github.semanticpie.pietunes.recommendation_service.services;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.PreferredGenre;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.UserNeo4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

public interface RecommendationService {


    Mono<Playlist> findPlaylistById(UUID id);

    Flux<Playlist> findPlaylistsAndSortByDate(UUID personId, String type);
}
