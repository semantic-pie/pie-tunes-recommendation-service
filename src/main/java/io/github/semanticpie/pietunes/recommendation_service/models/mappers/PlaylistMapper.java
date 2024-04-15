package io.github.semanticpie.pietunes.recommendation_service.models.mappers;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.NestedPlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.dtos.PlaylistDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {MusicTrackMapper.class})
public interface PlaylistMapper {

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "tracks", source = "tracks", qualifiedByName = "containedTracksToMusicDtos")
    PlaylistDTO toDTO(Playlist source);

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    NestedPlaylistDTO toNestedPlaylistDTO(Playlist source);
}
