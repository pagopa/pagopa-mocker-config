package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * The model that define the rule a passed request can follow in order
 * to obtain the defined mocked response.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The rule a passed request can follow in order to obtain the defined mocked response.")
public class MockRule implements Serializable {

    @JsonProperty("id")
    @Schema(description = "The unique identifier of the mock rule", example = "6c08a21c-6a92-4f6b-a1e1-bf68c4e099c9")
    private String id;

    @JsonProperty("name")
    @Schema(description = "The name or description related to the mock rule, for human readability.", example = "Main rule")
    @NotNull
    private String name;

    @JsonProperty("order")
    @Schema(description = "The cardinal order on which the mock role will be evaluated by Mocker.", example = "1")
    @NotNull
    private Integer order;

    @JsonProperty("is_active")
    @Schema(description = "The status flag that permits to activate or not the mock rule for Mocker evaluation.", example = "true")
    @NotNull
    private Boolean isActive;

    @JsonProperty("tags")
    @Schema(description = "The set of tags on which the mock rule is related to.")
    @NotNull
    private List<String> tags;

    @JsonProperty("conditions")
    @Schema(description = "The list of condition related to the mock rule that will be evaluated in AND by the Mocker.")
    @NotNull
    private List<MockCondition> conditions;

    @JsonProperty("response")
    @Schema(description = "The mocked response that the Mocker will give as output if all the condition are verified for the mock rule.")
    @NotNull
    private MockResponse response;


    @Hidden
    public void setIdIfNull(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
