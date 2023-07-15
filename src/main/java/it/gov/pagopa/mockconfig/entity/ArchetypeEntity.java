package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
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
@Table(name = "archetype")
public class ArchetypeEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "subsystem_url")
    private String subsystemUrl;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "http_method")
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @OneToMany(targetEntity = ArchetypeParameterEntity.class, fetch = FetchType.LAZY, mappedBy = "archetype", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchetypeParameterEntity> parameters;

    @OneToMany(targetEntity = ArchetypeResponseEntity.class, fetch = FetchType.LAZY, mappedBy = "archetype", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchetypeResponseEntity> responses;

    @ManyToMany(targetEntity = ResourceTagEntity.class)
    @JoinTable(name = "archetype_tag",
            joinColumns = {@JoinColumn(name = "archetype_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private List<ResourceTagEntity> tags;

    @OneToMany(targetEntity = MockResourceEntity.class, fetch = FetchType.LAZY, mappedBy = "archetype")
    private List<MockResourceEntity> mockResources;
}
