package io.github.semanticpie.pietunes.recommendation_service.repositories;


import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicGenre;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MusicGenreRepository extends ReactiveNeo4jRepository<MusicGenre, String> {
    Mono<MusicGenre> findMusicGenreByName(String name);

    @Query("""
            match (u:User)-[:PREFERS_GENRE]->(g:Genre) return DISTINCT g
            """)
    Flux<MusicGenre> findUsersPrefferedGenres();
}
