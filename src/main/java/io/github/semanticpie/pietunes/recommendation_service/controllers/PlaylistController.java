package io.github.semanticpie.pietunes.recommendation_service.controllers;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.PlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.dtos.UpdatePlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.dtos.UserPlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.mappers.PlaylistMapper;
import io.github.semanticpie.pietunes.recommendation_service.services.impl.PlaylistService;
import io.github.semanticpie.pietunes.recommendation_service.services.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("api/v1/library/playlists/")
public class PlaylistController {

    private final PlaylistService playlistService;

    private final JwtTokenProvider jwtTokenProvider;

    private final PlaylistMapper mapper;

    @GetMapping("{uuid}")
    @Parameter(in = ParameterIn.PATH, name = "uuid", description = "Playlist uuid")
    public Mono<PlaylistDTO> getPlaylist(@PathVariable("uuid") UUID uuid) {
        return playlistService.findPlaylistById(uuid).map(mapper::toDTO);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<PlaylistDTO>> createPlaylist(@RequestBody UserPlaylistDTO playlistDTO, ServerWebExchange exchange) {
        String jwtToken = jwtTokenProvider.getJwtTokenFromRequest(exchange.getRequest());
        UUID userUuid = UUID.fromString(jwtTokenProvider.getUUID(jwtToken));
        return playlistService
                .createNewPlaylist(userUuid, playlistDTO).map(mapper::toDTO)
                .flatMap(playlist -> Mono.just(new ResponseEntity<>(playlist, HttpStatus.CREATED)));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<?>> updatePlaylist(@RequestBody UpdatePlaylistDTO playlistDTO, ServerWebExchange exchange) {
        String jwtToken = jwtTokenProvider.getJwtTokenFromRequest(exchange.getRequest());
        UUID userUuid = UUID.fromString(jwtTokenProvider.getUUID(jwtToken));
        return playlistService.userHasPlaylist(userUuid, playlistDTO.uuid())
                .flatMap(isHas -> {
                    if (isHas) {
                        return playlistService
                                .updatePlaylist(userUuid, playlistDTO).map(mapper::toDTO)
                                .flatMap(playlist -> Mono.just(new ResponseEntity<>(playlist, HttpStatus.OK)));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
                    }
                });
    }

    @DeleteMapping("{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @Parameter(in = ParameterIn.PATH, name = "uuid", description = "Playlist uuid")
    public Mono<ResponseEntity<Void>> updatePlaylist(@PathVariable("uuid") UUID playlistId, ServerWebExchange exchange) {
        String jwtToken = jwtTokenProvider.getJwtTokenFromRequest(exchange.getRequest());
        UUID userUuid = UUID.fromString(jwtTokenProvider.getUUID(jwtToken));
        return playlistService.userHasPlaylist(userUuid, playlistId)
                .flatMap(isHas -> {
                    if (isHas) {
                        return playlistService
                                .deletePlaylistById(playlistId)
                                .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
                    }
                });
    }


}
