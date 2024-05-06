package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicGenre;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.UserNeo4j;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.PreferredGenre;
import io.github.semanticpie.pietunes.recommendation_service.repositories.MusicGenreRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.PlaylistRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.TrackRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.UserNeo4jRepository;
import io.github.semanticpie.pietunes.recommendation_service.services.GeneratedPlaylistService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
public class GenrePlaylistService implements GeneratedPlaylistService {

    private final int PLAYLIST_SIZE = 9;

    MusicGenreRepository musicGenreRepository;

    UserNeo4jRepository userRepository;

    PlaylistRepository playlistRepository;

    TrackRepository trackRepository;

    @Override
    @Transactional
    public Mono<Void> generate(UserNeo4j user) {

        AtomicInteger index = new AtomicInteger();

        return playlistRepository.deleteAllByTypeAndUser(PlaylistType.GENRE_MIX.name(), user.getUuid())
                .then(Flux.fromIterable(user.getPreferredGenres())
                        .map(PreferredGenre::getGenre)
                        .flatMap(genre -> {

                            Playlist playlist = new Playlist(genre.getName().toUpperCase() + "Mix", PlaylistType.GENRE_MIX);

                            return trackRepository.findRandomMusicTrackByGenre(genre, PLAYLIST_SIZE).map(t -> {
                                ContainedTrack track = ContainedTrack.builder().track(t).index(index.get()).build();
                                index.incrementAndGet();
                                return track;
                            }).collectList().map(tracks -> {
                                playlist.setTracks(new HashSet<>(tracks));
                                playlist.setGenre(genre);
                                return playlist;
                            });
                        }).doOnNext(playlist -> log.info("Playlist {} created", playlist.getName())
                        ).map(playlist -> {
                            playlist.setUsers(Set.of(user));
                            return playlist;
                        }).doFinally(playlists -> log.info("All playlists created"))
                        .map(playlist -> playlistRepository.save(playlist)).then());
    }
}
