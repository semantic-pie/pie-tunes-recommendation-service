package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
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

    public Mono<Void> generatePlaylists() {

      return genrePlaylistService.generate().then();
    }

    private Playlist sortTracksByIndex(Playlist playlist) {
        assert playlist.getTracks() != null;
        playlist.setTracks(
                playlist.getTracks()
                        .stream()
                        .sorted(Comparator.comparing(ContainedTrack::getIndex))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
        return playlist;
    }

    @Override
    public Mono<Playlist> findPlaylistById(UUID id) {
        return playlistRepository.findById(id).map(this::sortTracksByIndex);
    }

    @Override
    public Flux<Playlist> findPlaylistsAndSortByDate(UUID personId, String type) {
        return playlistRepository.findAllByUserId(personId, type);
    }


}
