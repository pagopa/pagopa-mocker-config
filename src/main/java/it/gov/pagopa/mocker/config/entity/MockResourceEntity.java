package it.gov.pagopa.mocker.config.entity;

import it.gov.pagopa.mocker.config.model.enumeration.HttpMethod;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
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

    private HttpMethod httpMethod;

    private List<NameValueEntity> specialHeaders;

    private Boolean isActive;

    @Indexed(name = "name_idx")
    private String name;

    private String archetypeId;

    @Indexed(name = "tags_idx")
    private Set<String> tags;

    private List<MockRuleEntity> rules;
}
