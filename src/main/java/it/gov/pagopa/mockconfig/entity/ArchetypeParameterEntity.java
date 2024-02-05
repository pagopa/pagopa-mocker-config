package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.entity.embeddable.ArchetypeParameterKey;
import it.gov.pagopa.mockconfig.entity.embeddable.InjectableParameterKey;
import it.gov.pagopa.mockconfig.model.enumeration.ArchetypeParameterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "archetype_parameter")
public class ArchetypeParameterEntity implements Serializable {

    @EmbeddedId
    private ArchetypeParameterKey id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ArchetypeParameterType type;

    @ManyToOne(targetEntity = ArchetypeEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archetype_id", insertable = false, updatable = false)
    private ArchetypeEntity archetype;


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
        ArchetypeParameterEntity other = (ArchetypeParameterEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
