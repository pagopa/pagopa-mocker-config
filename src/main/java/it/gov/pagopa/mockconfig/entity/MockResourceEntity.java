package it.gov.pagopa.mockconfig.entity;

import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document("mock_resources")
@ToString
public class MockResourceEntity implements Serializable {

    @Id
    private String id;

    private String subsystemUrl;

    private String resourceUrl;

    private String action;

    private HttpMethod httpMethod;

    private Boolean isActive;

    private String name;

    private String archetypeId;

    private Set<String> tags;

    private List<MockRuleEntity> rules;
}
