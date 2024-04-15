package io.github.semanticpie.pietunes.recommendation_service.controllers;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.NestedPlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;
import io.github.semanticpie.pietunes.recommendation_service.models.dtos.PlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.mappers.PlaylistMapper;
import io.github.semanticpie.pietunes.recommendation_service.services.RecommendationService;
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


    private final RecommendationService recommendationService;

    private final PlaylistMapper mapper;

    @GetMapping("/playlists/daily-mix/generate")
    public Mono<String> generatePlaylist() {
        return recommendationService.generatePlaylists().then(Mono.just("GENERATED"));
    }

    @GetMapping("/playlists/{uuid}")
    public Mono<PlaylistDTO> getPlaylist(@PathVariable("uuid") UUID uuid) {
        return recommendationService.findPlaylistById(uuid).map(mapper::toDTO);
    }

    @GetMapping("/playlists/daily-mix/find-by-date")
    @Parameter(in = ParameterIn.QUERY, name = "userUuid")
    public Flux<NestedPlaylistDTO> getAllPlaylistListsByDate(@RequestParam UUID userUuid) {
        return recommendationService.findPlaylistsAndSortByDate(userUuid, PlaylistType.DAILY_MIX.name()).map(mapper::toNestedPlaylistDTO);
    }

    @GetMapping("/playlists/genre-mix/find-by-date")
    @Parameter(in = ParameterIn.QUERY, name = "userUuid")
    public Flux<NestedPlaylistDTO> getGenreMixPlaylistByDate(@RequestParam UUID userUuid) {
        return recommendationService.findPlaylistsAndSortByDate(userUuid, PlaylistType.GENRE_MIX.name()).map(mapper::toNestedPlaylistDTO);
    }


}
