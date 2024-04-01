package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.PreferredGenre;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.UserNeo4j;
import io.github.semanticpie.pietunes.recommendation_service.repositories.PlaylistRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.TrackRepository;
import io.github.semanticpie.pietunes.recommendation_service.services.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final int PLAYLIST_SIZE = 9;

    private final TrackRepository trackRepository;

    private final PlaylistRepository playlistRepository;

    @Override
    public Mono<Playlist> generatePlaylist(UserNeo4j user) {

        Playlist playlist = new Playlist("Best playlist");

        AtomicInteger index = new AtomicInteger();

        Set<PreferredGenre> preferredGenres = user.getPreferredGenres();

        int sum = preferredGenres.stream().map(PreferredGenre::getWeight).mapToInt(Integer::intValue).sum();

        return Flux.fromIterable(preferredGenres)
                .flatMap(genre -> trackRepository
                        .findRandomMusicTrackByGenre(
                                genre.getGenre(), Math.round(((float) genre.getWeight() / sum) * PLAYLIST_SIZE)))
                .collectList()
                .flatMap(tracks -> {
                    Set<ContainedTrack> containedTracks = tracks.stream().map(t -> {
                        ContainedTrack track = ContainedTrack.builder().track(t).index(index.get()).build();
                        index.incrementAndGet();
                        return track;
                    }).collect(Collectors.toSet());

                    playlist.setTracks(containedTracks);
                    playlist.setUsers(Set.of(user));
                    return playlistRepository.save(playlist).map(this::sortTracksByIndex);
                });

    }

    private Playlist sortTracksByIndex(Playlist playlist) {
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
    public Flux<Playlist> findPlaylistsAndSortByDate(UUID personId) {
        return playlistRepository.findAllByUserId(personId);

    }


}
