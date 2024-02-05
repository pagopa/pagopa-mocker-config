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
@Table(name = "resource_tag")
public class ResourceTagEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "value")
    private String value;

    @ManyToMany(targetEntity = MockResourceEntity.class)
    @JoinTable(name = "mock_resource_tag",
            joinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "mock_resource_id", referencedColumnName = "id")})
    private List<MockResourceEntity> resources;

    @ManyToMany(targetEntity = ArchetypeEntity.class)
    @JoinTable(name = "archetype_tag",
            joinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "archetype_id", referencedColumnName = "id")})
    private List<ArchetypeEntity> archetypes;


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
        ResourceTagEntity other = (ResourceTagEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
