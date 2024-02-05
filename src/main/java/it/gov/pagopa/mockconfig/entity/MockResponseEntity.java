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
@Table(name = "mock_response")
public class MockResponseEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "body")
    private String body;

    @Column(name = "status")
    private int status;

    @OneToMany(targetEntity = InjectableParameterEntity.class, mappedBy = "response", cascade = CascadeType.ALL)
    private List<InjectableParameterEntity> parameters;

    @OneToMany(targetEntity = ResponseHeaderEntity.class, mappedBy = "response", cascade = CascadeType.ALL)
    private List<ResponseHeaderEntity> headers;

    @OneToOne(targetEntity = MockRuleEntity.class, fetch = FetchType.LAZY, optional = false, mappedBy = "response", cascade = CascadeType.ALL)
    private MockRuleEntity rule;


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
        MockResponseEntity other = (MockResponseEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
