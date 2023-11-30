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
@Table(name = "archetype_schema")
public class ArchetypeSchemaEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "subsystem")
    private String subsystem;

    @Column(name = "content")
    private String content;

    @OneToMany(targetEntity = ArchetypeResponseEntity.class, fetch = FetchType.LAZY, mappedBy = "schema", cascade = CascadeType.ALL)
    private List<ArchetypeResponseEntity> responses;
}
