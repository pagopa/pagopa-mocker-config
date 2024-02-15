package it.gov.pagopa.mocker.config.model.archetype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mocker.config.model.enumeration.HttpMethod;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class Archetype implements Serializable {

    @JsonProperty("id")
    @Schema(description = "The unique identifier of the archetype.", example = "2ee0aafa-9a9a-901a-bbb1-33394ff201ad")
    private String id;

    @JsonProperty("name")
    @Schema(description = "The name or description related to the archetype, for human readability.", example = "Get enrolled organization with ID 77777777777")
    @NotBlank(message = "The name to be assigned to the archetype cannot be null or blank.")
    private String name;

    @JsonProperty("subsystem")
    @Schema(description = "The URL section that define the subsystem on which the archetype is related.", example = "apiconfig/api/v1")
    @NotBlank(message = "The application or subsystem related to the archetype cannot be null or blank.")
    private String subsystem;

    @JsonProperty("resource_url")
    @Schema(description = "The specific URL on which the archetype will be defined for the subsystem.", example = "organizations/77777777777")
    @NotBlank(message = "The URL related to the archetype cannot be null or blank.")
    private String resourceURL;

    @JsonProperty("http_method")
    @Schema(description = "The HTTP method related to the archetype.", example = "GET")
    @NotNull(message = "The HTTP method related to the archetype cannot be null.")
    private HttpMethod httpMethod;

    @JsonProperty("tags")
    @Schema(description = "The set of tags on which the archetype is related to.")
    @NotNull(message = "The list of tags to be assigned the archetype cannot be null.")
    private List<String> tags;

    @JsonProperty("url_parameters")
    @Schema(description = "The list of parameter related to the archetype URL that must be set in mock resource creation.")
    @NotNull(message = "The list of parameter to be assigned to the archetype URL cannot be null.")
    @Valid
    private List<String> urlParameters;

    @JsonProperty("responses")
    @Schema(description = "The list of responses related to the archetype that can be edited in mock resource creation.")
    @NotNull(message = "The list of responses to be assigned to the archetype cannot be null.")
    @Valid
    private List<ArchetypeResponse> responses;
}
