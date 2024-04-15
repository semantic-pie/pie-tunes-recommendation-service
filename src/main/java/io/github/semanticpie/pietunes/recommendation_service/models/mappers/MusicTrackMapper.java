package io.github.semanticpie.pietunes.recommendation_service.models.mappers;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.MusicTrackDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.ContainedTrack;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicTrack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", uses = {MusicBandMapper.class, MusicAlbumMapper.class})
public interface MusicTrackMapper {


    @Named("MusicTrackToDto")
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "releaseYear", source = "releaseYear")
    @Mapping(target = "bitrate", source = "bitrate")
    @Mapping(target = "lengthInMilliseconds", source = "lengthInMilliseconds")
    @Mapping(target = "musicAlbum", source = "musicAlbum", qualifiedByName = "MusicAlbumToDto")
    @Mapping(target = "musicBand", source = "musicBand", qualifiedByName = "MusicBandToDto")
    MusicTrackDTO toDTO(MusicTrack source);

    @Named("containedTracksToMusicDtos")
    default List<MusicTrackDTO> convertContainedTracksToMusicTrackDTO(Set<ContainedTrack> source) {
        return source.stream().map(ContainedTrack::getTrack).map(this::toDTO).toList();
    }


}
