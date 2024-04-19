package io.github.semanticpie.pietunes.recommendation_service.repositories;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.Playlist;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlaylistRepository extends ReactiveNeo4jRepository<Playlist, UUID> {

    @Query("""
                MATCH p=(playlist:Playlist {type: :#{#type} })<-[:HAS_PLAYLIST]-(user:User {uuid: :#{#userId}})
                RETURN playlist, nodes(p), relationships(p)
                ORDER BY playlist.createdAt DESC
            """)
    Flux<Playlist> findAllByUserId(@Param("userId") UUID userId, @Param("type") String type);

    Mono<Void> deleteAllByType(@Param("type") String type);

    @Query("""
            MATCH (:User {uuid: :#{#userId}})-[r:HAS_PLAYLIST]-(:Playlist {uuid: :#{#playlistId}})
            RETURN COUNT(r) > 0 AS relationship_exists
            """)
    Mono<Boolean> userHasPlaylist(@Param("userId") UUID userId, @Param("playlistId") UUID playlistId);

}
