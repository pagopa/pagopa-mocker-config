package it.gov.pagopa.mocker.config.model.mockresource;

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
 * The model that define the general information about mock resource,
 * used for partial update.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The general information about mock resource, used for partial update.")
public class MockResourceGeneralInfo implements Serializable {

    @JsonProperty("name")
    @Schema(description = "The name or description related to the mock resources, for human readability.", example = "Get enrolled organization with ID 77777777777")
    @NotBlank(message = "The name to be assigned to the mock resource cannot be null or blank.")
    private String name;

    @JsonProperty("is_active")
    @Schema(description = "The status flag that permits to activate or not the mock resource for Mocker evaluation.", example = "true")
    @NotNull(message = "The activation flag for the mock resource cannot be null.")
    private Boolean isActive;

    @JsonProperty("tags")
    @Schema(description = "The set of tags on which the mock resource is related to.")
    @NotNull(message = "The list of tags to be assigned the mock resource cannot be null.")
    private List<String> tags;
}
