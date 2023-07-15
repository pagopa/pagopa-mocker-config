package it.gov.pagopa.mockconfig.model.archetype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
public class ArchetypeResponse implements Serializable {

    @JsonProperty("id")
    @Schema(description = "The unique identifier of the archetype response.", example = "2ee0aafa-9a9a-901a-bbb1-33394ff201ad")
    private String id;

    @JsonProperty("body")
    @Schema(description = "")
    private String body;

    @JsonProperty("status")
    @Schema(description = "")
    @NotNull(message = "")
    private int status;

    @JsonProperty("headers")
    @Schema(description = "")
    @NotNull(message = "")
    @Valid
    private List<ArchetypeResponseHeader> headers;

    @JsonProperty("injectable_parameters")
    @Schema(description = "")
    @NotNull(message = "")
    private List<String> injectableParameters;
}
