package it.gov.pagopa.mocker.config.model.script;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * The model that contains the complete list of scripts to be used for dynamic mock behavior.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The complete list of scripts to be used for dynamic mock behavior.")
public class ScriptMetadataList implements Serializable {

    @JsonProperty("scripts")
    @NotNull
    @Schema(description = "The list of retrieved scripts.")
    private List<ScriptMetadata> scripts;
}
