package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * The model that define the mock resource, analyzed by Mocker, that permits
 * to generate different responses based by rules and conditions.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The mock resource, analyzed by Mocker, that permits to generate different responses based by rules and conditions.")
public class MockResource implements Serializable {

    @JsonProperty("id")
    @Schema(description = "The unique identifier of the mock resource.", example = "70aa0abb-0001-0212-aa01-2cb1a0f00301")
    private String id;

    @JsonProperty("name")
    @Schema(description = "The name or description related to the mock resources, for human readability.", example = "Get enrolled organization with ID 77777777777")
    @NotNull
    private String name;

    @JsonProperty("subsystem")
    @Schema(description = "The URL section that define the subsystem on which the mock resource is related.", example = "apiconfig/api/v1")
    @NotNull
    private String subsystem;

    @JsonProperty("resource_url")
    @Schema(description = "The specific URL on which the mock resource will be defined for the subsystem.", example = "organizations/77777777777")
    @NotNull
    private String resourceURL;

    @JsonProperty("http_method")
    @Schema(description = "The HTTP method related to the mock resource.", example = "GET")
    @NotNull
    private HttpMethod httpMethod;

    @JsonProperty("is_active")
    @Schema(description = "The status flag that permits to activate or not the mock resource for Mocker evaluation.", example = "true")
    @NotNull
    private Boolean isActive;

    @JsonProperty("tags")
    @Schema(description = "The set of tags on which the mock resource is related to.")
    @NotNull
    private List<String> tags;

    @JsonProperty("rules")
    @Schema(description = "The list of rules related to the mock resource that will be evaluated by the Mocker.")
    @NotNull
    @Valid
    private List<MockRule> rules;

    @Hidden
    public void setIdIfNull(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
