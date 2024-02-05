package it.gov.pagopa.mockconfig.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "mock_rule")
public class MockRuleEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "\"order\"")
    private int order;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "resource_id", insertable = false, updatable = false)
    private String resourceId;

    @Column(name = "response_id", insertable = false, updatable = false)
    private String responseId;

    @ManyToMany(targetEntity = RuleTagEntity.class)
    @JoinTable(name = "mock_rule_tag",
            joinColumns = {@JoinColumn(name = "mock_rule_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private List<RuleTagEntity> tags;

    @OneToMany(targetEntity = MockConditionEntity.class, fetch = FetchType.EAGER, mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MockConditionEntity> conditions;

    @OneToOne(targetEntity = MockResponseEntity.class, fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "response_id")
    private MockResponseEntity response;

    @ManyToOne(targetEntity = MockResourceEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id")
    private MockResourceEntity resource;


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
        MockRuleEntity other = (MockRuleEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
