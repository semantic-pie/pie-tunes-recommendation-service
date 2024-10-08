package io.github.semanticpie.pietunes.recommendation_service.models.neo4j;

import lombok.*;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class PreferredGenre {

    @RelationshipId
    private Long id;

    @TargetNode
    @NonNull
    private MusicGenre genre;

    @NonNull
    private Integer weight;

    @Override
    public String toString() {
        return "PreferredGenre{" +
                "weight=" + weight +
                ", genre=" + genre +
                '}';
    }
}
