package io.github.semanticpie.pietunes.recommendation_service.repositories;


import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicGenre;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicTrack;
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
                         }],
                         Track_CONTAINS_Album: [(musicTrack)<-[:CONTAINS]-(album:Album) | album{
                             .description,
                             .name,
                             .uuid,
                             .version,
                             .yearOfRecord,
                             __nodeLabels__: labels(album),
                             __elementId__: id(album)
                         }],
                          Track_HAS_TRACK_Band: [(musicTrack)<-[:HAS_TRACK]-( musicTrack_musicBand:Band) | musicTrack_musicBand{
                            .description,
                            .name,
                            .uuid,
                            .version,
                            __nodeLabels__: labels( musicTrack_musicBand),
                            __elementId__: id( musicTrack_musicBand)
                         }]
                        }
                ORDER BY rand() limit :#{#limit}
            """)
    Flux<MusicTrack> findRandomMusicTrackByGenre(@Param("musicGenre") MusicGenre musicGenre, @Param("limit") int limit);

}
