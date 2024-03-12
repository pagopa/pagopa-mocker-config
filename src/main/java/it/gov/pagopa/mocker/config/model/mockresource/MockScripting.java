package it.gov.pagopa.mocker.config.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * The model that define the information about the script to be
 * executed by Mocker if all the conditions occurs.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The information about the script to be executed by Mocker if all the conditions occurs.")
public class MockScripting implements Serializable {

    @JsonProperty("script_name")
    @Schema(description = "The name of the script that will be executed by the Mocker for the associated rule.", example = "delay")
    @NotNull(message = "The name of the script that will be executed by the Mocker for the associated rule cannot be null.")
    private String scriptName;

    @JsonProperty("description")
    @Schema(description = "The human-readable description that explains the behavior of the script.", example = "Lorem ipsum sit dolorem")
    private String description;

    @JsonProperty("is_active")
    @Schema(description = "The flag that define if the scripting process is enabled for the associated rule.", example = "true")
    @NotNull(message = "The flag that define if the scripting process is enabled for the associated rule cannot be null.")
    private Boolean isActive;

    @JsonProperty("input_parameters")
    @Schema(description = "The list of parameters to be set as input for the execution of the script in the Mocker.")
    @NotNull(message = "The list of parameters to be set as input for the execution cannot be null.")
    private List<ScriptParameter> inputParameters;

    @JsonProperty("output_parameters")
    @Schema(description = "The list of parameters that will be returned as output from the script during runtime.", example = "dynamic.res")
    private List<String> outputParameters;
}
