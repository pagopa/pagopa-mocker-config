package it.gov.pagopa.mockconfig.model.archetype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The model that define the header that will be set to a mocked response generated by the archetype
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The header that will be set to a mocked response generated by the archetype")
public class ArchetypeResponseHeader implements Serializable {

    @JsonProperty("name")
    @Schema(description = "The key of the header to be set to archetype.", example = "Content-Type")
    @NotNull(message = "The name of the header to be assigned to the archetype cannot be null.")
    @NotBlank(message = "The name of the header to be assigned to the archetype cannot be blank.")
    private String name;

    @JsonProperty("value")
    @Schema(description = "The value of the header to be set to archetype.", example = "application/json")
    @NotNull(message = "The value of the header to be assigned to the archetype cannot be null.")
    private String value;
}