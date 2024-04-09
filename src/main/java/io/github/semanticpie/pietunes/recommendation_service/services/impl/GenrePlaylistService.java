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
import java.util.stream.Collectors;

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

        Flux<Playlist> genreMixes = playlistRepository.findAll().filter(playlist -> playlist.getType().equals(PlaylistType.GENRE_MIX));

        AtomicInteger index = new AtomicInteger();

        Flux<UserNeo4j> users = userRepository.findAll();

        Flux<MusicGenre> usersGenres = users
                .map(userNeo4j -> userNeo4j.getPreferredGenres()
                        .stream()
                        .map(PreferredGenre::getGenre)
                        .collect(Collectors.toSet()))
                .flatMapIterable(set -> set)
                .distinct();


        return playlistRepository.deleteAll(genreMixes).then(Flux.defer(() ->
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
                                })
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
                )
                .collectList().flatMap(playlists -> playlistRepository.saveAll(playlists).then()));
    }
}
