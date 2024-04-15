package io.github.semanticpie.pietunes.recommendation_service.models.mappers;

import io.github.semanticpie.pietunes.recommendation_service.models.dtos.MusicBandDTO;
import io.github.semanticpie.pietunes.recommendation_service.models.neo4j.MusicBand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MusicBandMapper {
    @Named("MusicBandToDto")
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    MusicBandDTO toDTO(MusicBand source);
}
