package io.github.semanticpie.pietunes.recommendation_service.repositories;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.MusicGenre;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.MusicTrack;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.UUID;


public interface TrackRepository extends ReactiveNeo4jRepository<MusicTrack, UUID> {

    @Query("""
    MATCH (genre:Genre {name: :#{#musicGenre.name}})<-[:IN_GENRE]-(musicTrack:Track)
    RETURN musicTrack{
             .bitrate,
             .lengthInMilliseconds,
             .releaseYear,
             .title,
             .uuid,
             .version,
             __nodeLabels__: labels(musicTrack),
             __elementId__: id(musicTrack),
             Track_IN_GENRE_Genre: [(musicTrack)-[:IN_GENRE]->(musicTrack_genres:Genre) | musicTrack_genres{
                 .name,
                 .version,
                 __nodeLabels__: labels(musicTrack_genres),
                 __elementId__: id(musicTrack_genres)
             }]
            }
    ORDER BY rand() limit :#{#limit}
""")
    Flux<MusicTrack> findRandomMusicTrackByGenre(@Param("musicGenre") MusicGenre musicGenre, @Param("limit") int limit);

}
