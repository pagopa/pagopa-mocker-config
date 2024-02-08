package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "mock_resource")
public class MockResourceEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "subsystem_url")
    private String subsystemUrl;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "action")
    private String action;

    @Column(name = "http_method")
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "name")
    private String name;

    @Column(name = "archetype_id", insertable = false, updatable = false)
    private String archetypeId;

    @OneToMany(targetEntity = MockRuleEntity.class, mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<MockRuleEntity> rules;

    @ManyToMany(targetEntity = ResourceTagEntity.class)
    @JoinTable(name = "mock_resource_tag",
            joinColumns = {@JoinColumn(name = "mock_resource_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private List<ResourceTagEntity> tags;

    @OneToOne(targetEntity = ArchetypeEntity.class, fetch = FetchType.EAGER)
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
        MockResourceEntity other = (MockResourceEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
