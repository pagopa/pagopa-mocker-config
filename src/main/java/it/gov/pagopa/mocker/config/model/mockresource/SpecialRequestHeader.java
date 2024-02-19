package it.gov.pagopa.mocker.config.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * The model that define the header that will be set to a mocked response
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The special header that can be added to a request in order to better reference a mock resource.")
public class SpecialRequestHeader implements Serializable {

    @JsonProperty("name")
    @Schema(description = "The key of the special header to be set to request to retrieve mock response from Mocker.", example = "SOAPAction")
    @NotNull(message = "The name of the special header to be assigned to the mock resource cannot be null.")
    @NotBlank(message = "The name of the special header to be assigned to the mock resource cannot be blank.")
    private String name;

    @JsonProperty("value")
    @Schema(description = "The value of the special header to be set to request to retrieve mock response from Mocker.", example = "someSoapAction")
    @NotNull(message = "The value of the special header to be assigned to the mock resource cannot be null.")
    private String value;
}
