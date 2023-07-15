package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.entity.embeddable.ArchetypeResponseHeaderKey;
import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "archetype_response_header")
public class ArchetypeResponseHeaderEntity implements Serializable {

    @EmbeddedId
    private ArchetypeResponseHeaderKey id;

    @Column(name = "value")
    private String value;

    @ManyToOne(targetEntity = ArchetypeResponseEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archetype_response_id", insertable = false, updatable = false)
    private ArchetypeResponseEntity response;
}
