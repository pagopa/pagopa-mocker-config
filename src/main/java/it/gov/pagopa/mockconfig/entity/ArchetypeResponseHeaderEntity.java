package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.entity.embeddable.ArchetypeResponseHeaderKey;
import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "archetype_response_header")
public class ArchetypeResponseHeaderEntity implements Serializable {

    @EmbeddedId
    private ArchetypeResponseHeaderKey id;

    @Column(name = "value")
    private String value;

    @ManyToOne(targetEntity = ArchetypeResponseEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archetype_response_id", insertable = false, updatable = false)
    private ArchetypeResponseEntity response;


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArchetypeResponseHeaderEntity other = (ArchetypeResponseHeaderEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
