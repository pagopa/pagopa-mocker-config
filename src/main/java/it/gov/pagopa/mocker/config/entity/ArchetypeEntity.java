package it.gov.pagopa.mocker.config.entity;

import it.gov.pagopa.mocker.config.model.enumeration.HttpMethod;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document("archetypes")
@ToString
public class ArchetypeEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private String subsystemUrl;

    private String resourceUrl;

    private String action;

    private HttpMethod httpMethod;

    private List<ArchetypeParameterEntity> parameters;

    private List<ArchetypeResponseEntity> responses;

    private List<String> tags;
}
