package io.github.semanticpie.pietunes.recommendation_service.controllers;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.MusicTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.repositories.TrackRepository;
import io.github.semanticpie.pietunes.recommendation_service.services.RecommendationService;
import io.github.semanticpie.pietunes.recommendation_service.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/v1")
public class PlaylistController {

    private final UserService userService;

    private final RecommendationService recommendationService;

    @GetMapping("/playlists/random/generate")
    @Parameter(in = ParameterIn.QUERY, name = "userUuid", schema = @Schema(type = "playlistUuid"))
    public Mono<Playlist> generatePlaylist(@RequestParam UUID userUuid) {
        return userService.findUserById(userUuid).flatMap(recommendationService::generatePlaylist);
    }

    @GetMapping("/playlists/{uuid}")
    @Parameter(in = ParameterIn.QUERY, name = "userUuid", schema = @Schema(type = "playlistUuid"))
    public Mono<Playlist> getPlaylist(@PathVariable("uuid") UUID uuid) {
        return recommendationService.findPlaylistById(uuid);
    }

    @GetMapping("/playlists/random/find-by-date")
    @Parameter(in = ParameterIn.QUERY, name = "userUuid", schema = @Schema(type = "playlistUuid"))
    public Flux<Playlist> getAllPlaylistListsByDate(@RequestParam UUID userUuid) {
        return recommendationService.findPlaylistsAndSortByDate(userUuid);
    }

}
