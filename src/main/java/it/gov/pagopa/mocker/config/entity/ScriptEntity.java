package it.gov.pagopa.mocker.config.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document("scripts")
@ToString
public class ScriptEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private Boolean selectable;

    private List<String> inputParameters;

    private List<String> outputParameters;

    private String code;
}
