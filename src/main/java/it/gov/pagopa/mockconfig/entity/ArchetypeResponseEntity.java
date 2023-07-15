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
@Table(name = "archetype_response")
public class ArchetypeResponseEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "body")
    private String body;

    @Column(name = "status")
    private int status;

    @Column(name = "archetype_id", insertable = false, updatable = false)
    private String archetypeId;

    @OneToMany(targetEntity = ArchetypeResponseHeaderEntity.class, mappedBy = "response", cascade = CascadeType.ALL)
    private List<ArchetypeResponseHeaderEntity> headers;

    @ManyToOne(targetEntity = ArchetypeEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archetype_id")
    private ArchetypeEntity archetype;
}
