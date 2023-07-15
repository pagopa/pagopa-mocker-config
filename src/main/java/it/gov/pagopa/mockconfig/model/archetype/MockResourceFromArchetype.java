package it.gov.pagopa.mockconfig.model.archetype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
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
public class MockResourceFromArchetype implements Serializable {

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("url_parameters")
    @Valid
    private List<StaticParameterValue> urlParameters;

    @JsonProperty("rules")
    @Valid
    private List<MockRuleFromArchetype> rules;
}
