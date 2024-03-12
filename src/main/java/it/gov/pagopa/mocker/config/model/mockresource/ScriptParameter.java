package it.gov.pagopa.mocker.config.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The model that define the parameter to be set as input for the execution of the script in the Mocker
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The parameter to be set as input for the execution of the script in the Mocker.")
public class ScriptParameter implements Serializable {

    @JsonProperty("name")
    @Schema(description = "The name of the parameter to be set as input. In script, it will be retrieved wit the annotation parameters.FIELD_NAME.", example = "some-param")
    @NotNull(message = "The name of the parameter to be assigned as input for the script cannot be null.")
    @NotBlank(message = "The name of the parameter to be assigned as input for the script cannot be blank.")
    private String name;

    @JsonProperty("value")
    @Schema(description = "The value of the parameter to be set as input. The value can be a constant or a dynamic value, retrievable from request body.", example = "some-value")
    @NotNull(message = "The value of the parameter to be assigned as input for the script cannot be null.")
    private String value;
}
