package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * The model that contains the information related to the mock resource that
 * will be analyzed by the Mocker analyzer.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MockResource implements Serializable {

    @JsonProperty("id")
    @Schema(description = "", example = "")
    private String id;

    @JsonProperty("name")
    @Schema(description = "", example = "")
    @NotNull
    private String name;

    @JsonProperty("subsystem")
    @Schema(description = "", example = "")
    @NotNull
    private String subsystem;

    @JsonProperty("resource_url")
    @Schema(description = "", example = "")
    @NotNull
    private String resourceURL;

    @JsonProperty("http_method")
    @Schema(description = "", example = "")
    @NotNull
    private HttpMethod httpMethod;

    @JsonProperty("is_active")
    @Schema(description = "", example = "")
    @NotNull
    private Boolean isActive;

    @JsonProperty("tags")
    @Schema(description = "", example = "")
    @NotNull
    private List<String> tags;

    @JsonProperty("rules")
    @Schema(description = "", example = "")
    @NotNull
    @Valid
    private List<MockRule> rules;
}
