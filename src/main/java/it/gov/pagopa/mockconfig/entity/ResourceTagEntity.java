package it.gov.pagopa.mockconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
}
