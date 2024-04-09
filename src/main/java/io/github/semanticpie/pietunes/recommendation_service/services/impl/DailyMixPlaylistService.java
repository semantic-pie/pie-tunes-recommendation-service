package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.PreferredGenre;
import io.github.semanticpie.pietunes.recommendation_service.repositories.PlaylistRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.TrackRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.UserNeo4jRepository;
import io.github.semanticpie.pietunes.recommendation_service.services.PlaylistService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DailyMixPlaylistService implements PlaylistService {

    private final int PLAYLIST_SIZE = 9;

    UserNeo4jRepository userRepository;

    PlaylistRepository playlistRepository;

    TrackRepository trackRepository;

    @Override
    @Transactional
    public Mono<Void> generate() {

        return userRepository.findAll().flatMap(user -> {

            Set<PreferredGenre> preferredGenres = user.getPreferredGenres();

            Set<Playlist> userDailyMixPlaylists =
                    user.getPlaylists().stream()
                            .filter(playlist -> playlist.getType().equals(PlaylistType.DAILY_MIX))
                            .collect(Collectors.toSet());

            return playlistRepository.deleteAll(userDailyMixPlaylists).then(Mono.defer(() ->
                    Flux.fromIterable(Set.of(
                                    new Playlist("DailyMix 1", PlaylistType.DAILY_MIX, Set.of(user)),
                                    new Playlist("DailyMix 2", PlaylistType.DAILY_MIX, Set.of(user)),
                                    new Playlist("DailyMix 3", PlaylistType.DAILY_MIX, Set.of(user)),
                                    new Playlist("DailyMix 4", PlaylistType.DAILY_MIX, Set.of(user)),
                                    new Playlist("DailyMix 5", PlaylistType.DAILY_MIX, Set.of(user))))
                            .flatMap(playlist -> generatePlaylistByGenres(preferredGenres, playlist)).collectList()
                            .flatMap(playlist -> playlistRepository.saveAll(playlist).then())
            )).then();
        }).then();
    }

    private Mono<Playlist> generatePlaylistByGenres(Set<PreferredGenre> preferredGenres, Playlist playlist) {

        int sum = preferredGenres.stream().map(PreferredGenre::getWeight).mapToInt(Integer::intValue).sum();

        AtomicInteger index = new AtomicInteger();
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
                    return Mono.just(playlist);
                });
    }
}
