package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.PlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.dtos.UpdatePlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.dtos.UserPlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.repositories.PlaylistRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.TrackRepository;
import io.github.semanticpie.pietunes.recommendation_service.repositories.UserNeo4jRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final TrackRepository trackRepository;

    private final UserNeo4jRepository userRepository;

    public Mono<Playlist> findPlaylistById(UUID id) {
        return playlistRepository.findById(id).map(this::sortTracksByIndex);
    }

    @Transactional
    public Mono<Playlist> createNewPlaylist(UUID userUUID, UserPlaylistDTO playlistDTO) {
        return userRepository
                .findUserNeo4jByUuid(userUUID)
                .flatMap(userNeo4j -> {
                    AtomicInteger index = new AtomicInteger();
                    Playlist playlist = new Playlist(playlistDTO.name(), PlaylistType.USER_PLAYLIST, Set.of(userNeo4j));
                    return trackRepository
                            .findAllByUuidIn(playlistDTO.tracks())
                            .map(track -> ContainedTrack.builder().track(track).index(index.getAndIncrement()).build())
                            .collectList()
                            .doOnNext(tracks -> log.info("Tracks: {}", tracks.toString()))
                            .doOnNext(tracks -> playlist.setTracks(new HashSet<>(tracks)))
                            .then(playlistRepository.save(playlist));
                });
    }

    public Mono<Boolean> userHasPlaylist(UUID userUUID, UUID playlistUUID) {
        return playlistRepository.userHasPlaylist(userUUID, playlistUUID);
    }

    @Transactional
    public Mono<Playlist> updatePlaylist(UUID userUUID, UpdatePlaylistDTO playlistDTO) {
        return userRepository
                .findUserNeo4jByUuid(userUUID)
                .flatMap(userNeo4j -> {
                    AtomicInteger index = new AtomicInteger();

                    return playlistRepository.findById(playlistDTO.uuid()).map(playlist -> {
                        playlist.setType(PlaylistType.USER_PLAYLIST);
                        playlist.setUsers(Set.of(userNeo4j));
                        playlist.setName(playlistDTO.name());
                        return playlist;
                    }).flatMap(playlist -> trackRepository
                            .findAllByUuidIn(playlistDTO.tracks())
                            .map(track -> ContainedTrack.builder().track(track).index(index.getAndIncrement()).build())
                            .collectList()
                            .doOnNext(tracks -> playlist.setTracks(new HashSet<>(tracks)))
                            .thenReturn(playlist))
                            .flatMap(playlistRepository::save);

                });
    }

    public Mono<Void> deletePlaylistById(UUID uuid) {
        return playlistRepository.deleteById(uuid);
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

}
