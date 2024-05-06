package io.github.semanticpie.pietunes.recommendation_service.services;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.UserNeo4j;
import reactor.core.publisher.Mono;

public interface GeneratedPlaylistService {

    Mono<Void> generate(UserNeo4j user);
}
