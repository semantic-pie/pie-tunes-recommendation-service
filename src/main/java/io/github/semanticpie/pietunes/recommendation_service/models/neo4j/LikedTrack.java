package io.github.semanticpie.pietunes.recommendation_service.models.neo4j;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class LikedTrack {

    @RelationshipId
    private Long id;

    @TargetNode
    @NonNull
    private MusicTrack track;

    @NonNull
    @CreatedDate
    private Long createdAt;
}
