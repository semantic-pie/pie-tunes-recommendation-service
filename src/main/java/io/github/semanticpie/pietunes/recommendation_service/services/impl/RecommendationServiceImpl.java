package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.repositories.PlaylistRepository;
import io.github.semanticpie.pietunes.recommendation_service.services.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final GenrePlaylistService genrePlaylistService;

    private final DailyMixPlaylistService dailyMixPlaylistService;

    PlaylistRepository playlistRepository;

    @Override
    public Mono<Void> generatePlaylists() {
      return dailyMixPlaylistService.generate().then(genrePlaylistService.generate()).then();
    }

    @Override
    public Flux<Playlist> findPlaylistsAndSortByDate(UUID personId, String type) {
        return playlistRepository.findAllByUserId(personId, type);
    }


}
