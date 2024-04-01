package io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;
import java.util.UUID;

@Node("Track")
@RequiredArgsConstructor
@Getter
@Setter
public class MusicTrack {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Version
    @JsonIgnore
    private Long version;

    private String title;

    private String releaseYear;

    private Integer bitrate;

    private Long lengthInMilliseconds;

    @Relationship(type = "IN_GENRE", direction = Relationship.Direction.OUTGOING)
    private Set<MusicGenre> genres;

    @Override
    public String toString() {
        return "MusicTrack{" +
                "genres=" + genres +
                ", title='" + title + '\'' +
                '}';
    }
}
