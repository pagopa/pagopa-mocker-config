package it.gov.pagopa.mockconfig.entity;

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
@Table(name = "archetype_response")
public class ArchetypeResponseEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status")
    private int status;

    @Column(name = "archetype_id", insertable = false, updatable = false)
    private String archetypeId;

    @Column(name = "schema_id", insertable = false, updatable = false)
    private String schemaId;

    @OneToOne(targetEntity = ArchetypeSchemaEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "schema_id")
    private ArchetypeSchemaEntity schema;

    @OneToMany(targetEntity = ArchetypeResponseHeaderEntity.class, fetch = FetchType.LAZY, mappedBy = "response", cascade = CascadeType.ALL)
    private List<ArchetypeResponseHeaderEntity> headers;

    @ManyToOne(targetEntity = ArchetypeEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archetype_id")
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
        ArchetypeResponseEntity other = (ArchetypeResponseEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
