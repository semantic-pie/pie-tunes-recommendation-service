package io.github.semanticpie.pietunes.recommendation_service.models.neo4j;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContainedTrack {
    @RelationshipId
    @JsonIgnore
    private Long id;


    @TargetNode
    @NonNull
    private MusicTrack track;

    @NonNull
    @JsonIgnore
    private Integer index;
}
