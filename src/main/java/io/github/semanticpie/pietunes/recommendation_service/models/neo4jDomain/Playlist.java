package io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Node("Playlist")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Playlist {

    @Id
    @GeneratedValue
    private UUID uuid;

    @NonNull
    @CreatedDate
    private Instant createdAt;

    @Version
    @JsonIgnore
    private Long version;

    @NonNull
    private String name;

    @NonNull
    private PlaylistType type;

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private Set<ContainedTrack> tracks;

    @JsonIgnore
    @Relationship(type = "HAS_PLAYLIST", direction = Relationship.Direction.INCOMING)
    private Set<UserNeo4j> users;

    public Playlist(@NonNull String name, @NonNull PlaylistType type, @NonNull Set<UserNeo4j> users) {
        this.name = name;
        this.type = type;
        this.users = users;
        this.createdAt = Instant.now();
    }




}
