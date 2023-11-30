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
public class ArchetypeResponseHeaderKey implements Serializable {

    @Column(name = "archetype_response_id", insertable = false, updatable = false)
    private String archetypeResponseId;

    @Column(name = "header")
    private String header;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchetypeResponseHeaderKey that = (ArchetypeResponseHeaderKey) o;
        return archetypeResponseId.equals(that.archetypeResponseId) && header.equals(that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(archetypeResponseId, header);
    }
}
