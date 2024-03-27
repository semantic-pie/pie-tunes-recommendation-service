package io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Node("Album")
@RequiredArgsConstructor
@Getter
@Setter
public class MusicAlbum {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Version
    private Long version;

    private String name;

    @Nullable
    private String description;

    @Nullable
    private int yearOfRecord;

    @Relationship(type = "HAS_ALBUM", direction = Relationship.Direction.INCOMING)
    private MusicBand musicBand;
}
