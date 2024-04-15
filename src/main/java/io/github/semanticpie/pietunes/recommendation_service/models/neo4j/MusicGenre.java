package io.github.semanticpie.pietunes.recommendation_service.models.neo4j;

import lombok.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "MusicGenre{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicGenre that = (MusicGenre) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
