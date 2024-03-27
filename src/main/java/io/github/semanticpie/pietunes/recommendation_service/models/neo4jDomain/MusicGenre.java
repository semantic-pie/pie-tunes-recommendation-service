package io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain;

import lombok.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Genre")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MusicGenre {

    @Version
    private Long version;

    @Id
    @NonNull
    private String name;

}
