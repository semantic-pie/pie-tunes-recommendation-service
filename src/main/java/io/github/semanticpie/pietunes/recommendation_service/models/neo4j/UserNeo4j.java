package io.github.semanticpie.pietunes.recommendation_service.models.neo4j;

import lombok.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;
import java.util.UUID;

@Node("User")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserNeo4j {

    private static final int INITIAL_GENRE_WEIGHT = 5;

    @Id
    @NonNull
    private UUID uuid;

    @Version
    private Long version;
    @DynamicLabels
    @Relationship(type = "PREFERS_GENRE", direction = Relationship.Direction.OUTGOING)
    private Set<PreferredGenre> preferredGenres;

    @DynamicLabels
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private Set<LikedTrack> likedTracks;

    @DynamicLabels
    @Relationship(type = "HAS_PLAYLIST", direction = Relationship.Direction.OUTGOING)
    private Set<Playlist> playlists;

}
