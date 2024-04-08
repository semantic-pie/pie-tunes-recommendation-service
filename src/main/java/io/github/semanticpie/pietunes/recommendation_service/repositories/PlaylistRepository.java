package io.github.semanticpie.pietunes.recommendation_service.repositories;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PlaylistRepository extends ReactiveNeo4jRepository<Playlist, UUID> {

    @Query("""
                MATCH p=(playlist:Playlist {type: :#{#type} })<-[:HAS_PLAYLIST]-(user:User {uuid: :#{#userId}})
                RETURN playlist, nodes(p), relationships(p)
                ORDER BY playlist.createdAt DESC
            """)
    Flux<Playlist> findAllByUserId(@Param("userId") UUID userId, @Param("type") String type);

}
