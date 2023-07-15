package it.gov.pagopa.mockconfig.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ArchetypeParameterKey implements Serializable {

    @Column(name = "archetype_id", insertable = false, updatable = false)
    private String archetypeId;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchetypeParameterKey that = (ArchetypeParameterKey) o;
        return archetypeId.equals(that.archetypeId) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(archetypeId, name);
    }
}
