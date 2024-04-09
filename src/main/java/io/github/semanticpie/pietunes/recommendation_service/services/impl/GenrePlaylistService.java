package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.*;
import io.github.semanticpie.pietunes.recommendation_service.repositories.MusicGenreRepository;
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

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@AllArgsConstructor
public class GenrePlaylistService implements PlaylistService {

    private final int PLAYLIST_SIZE = 9;

    MusicGenreRepository musicGenreRepository;

    UserNeo4jRepository userRepository;

    PlaylistRepository playlistRepository;

    TrackRepository trackRepository;

    @Override
    @Transactional
    public Mono<Void> generate() {

        AtomicInteger index = new AtomicInteger();

        Flux<UserNeo4j> users = userRepository.findAll();

        Flux<MusicGenre> usersGenres = musicGenreRepository.findUsersPrefferedGenres();

        return playlistRepository.deleteAllByType(PlaylistType.GENRE_MIX.name()).then(Flux.defer(() ->
                        usersGenres
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
                                }).doOnNext(playlist -> log.info("Playlist {} created", playlist.getName()))
                ).flatMap(playlist ->
                        users.filter(user ->
                                        user.getPreferredGenres()
                                                .stream()
                                                .map(PreferredGenre::getGenre)
                                                .toList().contains(playlist.getGenre())
                                )
                                .collectList()
                                .map(HashSet::new)
                                .map(matchedUsers -> {
                                    playlist.setUsers(matchedUsers);
                                    return playlist;
                                })
                ).doFinally(playlists -> log.info("All playlists created"))
                .collectList().flatMap(playlists -> playlistRepository.saveAll(playlists).then()));
    }
}
