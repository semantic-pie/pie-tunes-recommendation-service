package io.github.semanticpie.pietunes.recommendation_service.services;

import reactor.core.publisher.Mono;

public interface GeneratedPlaylistService {

    Mono<Void> generate();
}
