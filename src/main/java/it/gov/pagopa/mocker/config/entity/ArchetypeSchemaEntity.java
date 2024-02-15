package it.gov.pagopa.mocker.config.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document("archetype_schema")
@ToString
public class ArchetypeSchemaEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private String subsystem;

    private String content;
}
