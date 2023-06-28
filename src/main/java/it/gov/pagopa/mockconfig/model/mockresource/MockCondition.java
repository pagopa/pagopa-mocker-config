package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mockconfig.model.enumeration.ConditionType;
import it.gov.pagopa.mockconfig.model.enumeration.ContentType;
import it.gov.pagopa.mockconfig.model.enumeration.RuleFieldPosition;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MockCondition implements Serializable {

    @JsonProperty("id")
    @Schema(description = "", example = "")
    private String id;

    @JsonProperty("order")
    @Schema(description = "", example = "")
    private Integer order;

    @JsonProperty("field_position")
    @Schema(description = "", example = "")
    @NotNull
    private RuleFieldPosition fieldPosition;

    @JsonProperty("analyzed_content_type")
    @Schema(description = "", example = "")
    @NotNull
    private ContentType analyzedContentType;

    @JsonProperty("field_name")
    @Schema(description = "", example = "")
    @NotNull
    private String fieldName;

    @JsonProperty("condition_type")
    @Schema(description = "", example = "")
    @NotNull
    private ConditionType conditionType;

    @JsonProperty("condition_value")
    @Schema(description = "", example = "")
    private String conditionValue;

    public void setIdIfNull(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
