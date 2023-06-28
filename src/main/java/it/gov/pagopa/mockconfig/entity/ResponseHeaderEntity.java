package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
}
