package io.github.semanticpie.pietunes.recommendation_service.repositories;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.MusicTrack;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface TrackMetadataRepository extends ReactiveNeo4jRepository<MusicTrack, UUID> {
    Mono<MusicTrack> findByTitleAndMusicBand_Name(String title, String musicBand_Name);

    Mono<MusicTrack> findByUuid(UUID uuid);
}
