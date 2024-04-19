package io.github.semanticpie.pietunes.recommendation_service.repositories;


import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicGenre;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicTrack;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;


public interface TrackRepository extends ReactiveNeo4jRepository<MusicTrack, UUID> {

    @Query("""
                MATCH (genre:Genre {name: :#{#musicGenre.name}})<-[in_genre:IN_GENRE]-(musicTrack:Track)
                MATCH (musicTrack)<-[contains:CONTAINS]-(album:Album)
                MATCH (musicTrack)<-[has_track:HAS_TRACK]-( musicTrack_musicBand:Band)
                RETURN musicTrack, collect(in_genre), collect(genre), collect(contains), collect(album), collect(has_track), collect(musicTrack_musicBand)
                ORDER BY rand() limit :#{#limit}
            """)
    Flux<MusicTrack> findRandomMusicTrackByGenre(@Param("musicGenre") MusicGenre musicGenre, @Param("limit") int limit);

    @Transactional
    Flux<MusicTrack> findAllByUuidIn(List<UUID> uuids);
}
