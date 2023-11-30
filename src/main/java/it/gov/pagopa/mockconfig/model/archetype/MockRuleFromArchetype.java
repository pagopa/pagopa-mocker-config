package it.gov.pagopa.mockconfig.model.archetype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mockconfig.model.mockresource.MockCondition;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "")
public class MockRuleFromArchetype implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("order")
    private Integer order;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("conditions")
    private List<MockCondition> conditions;

    @JsonProperty("response")
    private MockResponseFromArchetype response;
}
