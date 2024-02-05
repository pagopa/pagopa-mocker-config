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
@Table(name = "archetype_schema")
public class ArchetypeSchemaEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "subsystem")
    private String subsystem;

    @Column(name = "content")
    private String content;

    @OneToMany(targetEntity = ArchetypeResponseEntity.class, fetch = FetchType.LAZY, mappedBy = "schema", cascade = CascadeType.ALL)
    private List<ArchetypeResponseEntity> responses;


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
        ArchetypeSchemaEntity other = (ArchetypeSchemaEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
