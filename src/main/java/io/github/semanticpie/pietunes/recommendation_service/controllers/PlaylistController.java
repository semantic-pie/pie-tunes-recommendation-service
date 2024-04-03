package io.github.semanticpie.pietunes.recommendation_service.controllers;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.Playlist;
import io.github.semanticpie.pietunes.recommendation_service.services.impl.RecommendationServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/api/v1/recommendations/")
public class PlaylistController {


    private final RecommendationServiceImpl recommendationService;

    @GetMapping("/playlists/daily-mix/generate")
    public Mono<String> generatePlaylist() {
        return recommendationService.generateDailyMixPlaylists().then(Mono.just("GENERATED"));
    }

    @GetMapping("/playlists/{uuid}")
    public Mono<Playlist> getPlaylist(@PathVariable("uuid") UUID uuid) {
        return recommendationService.findPlaylistById(uuid);
    }

    @GetMapping("/playlists/daily-mix/find-by-date")
    @Parameter(in = ParameterIn.QUERY, name = "userUuid")
    public Flux<Playlist> getAllPlaylistListsByDate(@RequestParam UUID userUuid) {
        return recommendationService.findPlaylistsAndSortByDate(userUuid);
    }


}
