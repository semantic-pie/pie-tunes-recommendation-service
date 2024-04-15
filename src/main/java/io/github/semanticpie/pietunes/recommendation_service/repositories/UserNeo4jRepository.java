package io.github.semanticpie.pietunes.recommendation_service.repositories;


import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.UserNeo4j;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserNeo4jRepository extends ReactiveNeo4jRepository<UserNeo4j, UUID> {
    Mono<UserNeo4j> findUserNeo4jByUuid(UUID uuid);


}