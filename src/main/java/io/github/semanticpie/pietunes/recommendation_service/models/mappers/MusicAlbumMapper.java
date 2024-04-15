package io.github.semanticpie.pietunes.recommendation_service.models.mappers;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.MusicAlbumDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicAlbum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MusicAlbumMapper {
    @Named("MusicAlbumToDto")
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "yearOfRecord", source = "yearOfRecord")
    MusicAlbumDTO toDTO(MusicAlbum source);
}
