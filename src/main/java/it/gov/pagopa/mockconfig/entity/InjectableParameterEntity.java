package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.entity.embeddable.InjectableParameterKey;
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
@Table(name = "injectable_parameter")
public class InjectableParameterEntity implements Serializable {

    @EmbeddedId
    private InjectableParameterKey id;

    @ManyToOne(targetEntity = MockResponseEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "response_id", insertable = false, updatable = false)
    private MockResponseEntity response;


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
        InjectableParameterEntity other = (InjectableParameterEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
