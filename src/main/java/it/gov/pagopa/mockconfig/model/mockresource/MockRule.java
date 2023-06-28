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
public class MockRule implements Serializable {

    @JsonProperty("id")
    @Schema(description = "", example = "")
    private String id;

    @JsonProperty("name")
    @Schema(description = "", example = "")
    @NotNull
    private String name;

    @JsonProperty("order")
    @Schema(description = "", example = "")
    @NotNull
    private Integer order;

    @JsonProperty("is_active")
    @Schema(description = "", example = "")
    @NotNull
    private Boolean isActive;

    @JsonProperty("tags")
    @Schema(description = "", example = "")
    @NotNull
    private List<String> tags;

    @JsonProperty("conditions")
    @Schema(description = "", example = "")
    @NotNull
    private List<MockCondition> conditions;

    @JsonProperty("response")
    @Schema(description = "", example = "")
    @NotNull
    private MockResponse response;


    public void setIdIfNull(String id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
