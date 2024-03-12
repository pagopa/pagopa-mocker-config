package it.gov.pagopa.mocker.config.model.script;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * The model that define the script that can be executed by Mocker and that permits
 * to generate dynamic values based on scripted execution.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The script that can be executed by Mocker and that permits to generate dynamic values based on scripted execution.")
public class ScriptMetadata implements Serializable {

    @JsonProperty("id")
    @Schema(description = "The unique identifier of the script.", example = "fb5363bcf68f687c9caeddbc221769f6")
    private String id;

    @JsonProperty("name")
    @Schema(description = "The name of the script.", example = "delay")
    @NotBlank(message = "The name associated to the script cannot be null or blank.")
    private String name;

    @JsonProperty("description")
    @Schema(description = "The human-readable description that explains the behavior of the script.", example = "Lorem ipsum sit dolorem")
    @NotBlank(message = "The name associated to the script cannot be null or blank.")
    private String description;

    @JsonProperty("input_parameters")
    @Schema(description = "The list of parameters to be passed as input to the script during runtime.", example = "name")
    @NotNull(message = "The list of parameters to be passed as input to the script during runtime cannot be null.")
    private List<String> inputParameters;

    @JsonProperty("output_parameters")
    @Schema(description = "The list of parameters that will be returned as output from the script during runtime.", example = "dynamic.res")
    @NotNull(message = "The list of parameters that will be returned as output from the script during runtime cannot be null.")
    private List<String> outputParameters;
}
