package it.gov.pagopa.mockconfig.model.mockresource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
public class MockResponse implements Serializable {

    @JsonProperty("id")
    @Schema(description = "", example = "")
    private String id;

    @JsonProperty("body")
    @Schema(description = "", example = "")
    private String body;

    @JsonProperty("status")
    @Schema(description = "", example = "")
    @NotNull
    private Integer status;

    @JsonProperty("headers")
    @Schema(description = "", example = "")
    @NotNull
    private List<ResponseHeader> headers;

    @JsonProperty("parameters")
    @Schema(description = "", example = "")
    @NotNull
    private List<String> parameters;


    public void setIdIfNull(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
