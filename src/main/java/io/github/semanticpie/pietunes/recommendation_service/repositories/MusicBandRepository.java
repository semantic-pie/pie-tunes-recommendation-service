package io.github.semanticpie.pietunes.recommendation_service.repositories;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.MusicBand;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface MusicBandRepository extends ReactiveNeo4jRepository<MusicBand, UUID> {
}
