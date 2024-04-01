package io.github.semanticpie.pietunes.recommendation_service.services;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.UserNeo4j;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {

    Mono<UserNeo4j> findUserById(UUID id);
}
