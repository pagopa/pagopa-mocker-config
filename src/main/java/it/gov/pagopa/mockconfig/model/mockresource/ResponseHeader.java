package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
@Schema(description = "The header that will be set to a mocked response")
public class ResponseHeader implements Serializable {

    @JsonProperty("name")
    @Schema(description = "The key of the header to be set to mock response by Mocker.", example = "Content-Type")
    @NotNull
    private String name;

    @JsonProperty("value")
    @Schema(description = "The value of the header to be set to mock response by Mocker.", example = "application/json")
    @NotNull
    private String value;
}
