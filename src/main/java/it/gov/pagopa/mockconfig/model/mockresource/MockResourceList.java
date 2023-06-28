package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.mockconfig.model.PageInfo;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * The model that contains the paginated list of mock resources.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The paginated list of mock resources.")
public class MockResourceList implements Serializable {

    @JsonProperty("resources")
    @NotNull
    @Schema(description = "The list of retrieved mock resources.")
    private List<MockResource> mockResources;

    @JsonProperty("page_info")
    @Schema(description = "The information related to the result page.")
    @NotNull
    @Valid
    private PageInfo pageInfo;
}
