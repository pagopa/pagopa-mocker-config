package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mockconfig.model.enumeration.ConditionType;
import it.gov.pagopa.mockconfig.model.enumeration.ContentType;
import it.gov.pagopa.mockconfig.model.enumeration.RuleFieldPosition;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The model that define the condition applied to the mock rule that a request
 * must respect in order to retrieve the mocked response related to the rule.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The condition applied to the mock rule that a request must respect in order to retrieve the mocked response related to the rule.")
public class MockCondition implements Serializable {

    @JsonProperty("id")
    @Schema(description = "The unique identifier of the mock condition.", example = "6b0b003d-74f4-428e-b950-61f42e02bf07")
    private String id;

    @JsonProperty("order")
    @Schema(description = "The cardinal order on which the mock condition will be evaluated by Mocker.", example = "")
    private Integer order;

    @JsonProperty("field_position")
    @Schema(description = "The parameter that define where the field/element that will be evaluated by this condition is located in the request.", example = "BODY")
    @NotNull
    private RuleFieldPosition fieldPosition;

    @JsonProperty("analyzed_content_type")
    @Schema(description = "The parameter that define the type of the source in the request where the field/element that will be evaluated by this condition will be extracted.", example = "JSON")
    @NotNull
    private ContentType analyzedContentType;

    @JsonProperty("field_name")
    @Schema(description = "The parameter that define the identifier of the field/element that will be evaluated by this condition", example = "station.id")
    @NotNull
    private String fieldName;

    @JsonProperty("condition_type")
    @Schema(description = "The parameter that define the type of condition that will be applied on the field/element that will be evaluated.", example = "EQ")
    @NotNull
    private ConditionType conditionType;

    @JsonProperty("condition_value")
    @Schema(description = "The parameter that define the value that the field/element must respect following the condition type.", example = "77777777777_01")
    private String conditionValue;


    @Hidden
    public void setIdIfNull(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
