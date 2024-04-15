package io.github.semanticpie.pietunes.recommendation_service.models.neo4j;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.semanticpie.pietunes.recommendation_service.models.enums.PlaylistType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.*;

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
    private Long createdAt;

    @Version
    @JsonIgnore
    private Long version;

    @NonNull
    private String name;

    @NonNull
    private PlaylistType type;
    
    @DynamicLabels
    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private Set<ContainedTrack> tracks;

    @DynamicLabels
    @JsonIgnore
    @Relationship(type = "HAS_PLAYLIST", direction = Relationship.Direction.INCOMING)
    private Set<UserNeo4j> users;


    @JsonIgnore
    @Relationship(type = "IN_GENRE", direction = Relationship.Direction.OUTGOING)
    private MusicGenre genre;

    public Playlist(@NonNull String name, @NonNull PlaylistType type, @NonNull Set<UserNeo4j> users) {
        this.name = name;
        this.type = type;
        this.users = users;
        this.createdAt = System.currentTimeMillis();
    }

    public Playlist(@NonNull String name, @NonNull PlaylistType type) {
        this.name = name;
        this.type = type;
        this.createdAt = System.currentTimeMillis();
    }



}
