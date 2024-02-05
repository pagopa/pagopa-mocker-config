package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "response_header")
public class ResponseHeaderEntity implements Serializable {

    @EmbeddedId
    private ResponseHeaderKey id;

    @Column(name = "value")
    private String value;

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
        ResponseHeaderEntity other = (ResponseHeaderEntity) obj;
        return Objects.equals(id, other.getId());
    }
}
