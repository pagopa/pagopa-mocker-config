package it.gov.pagopa.mocker.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfo {

  @JsonProperty("name")
  @Schema(description = "The name of the application.")
  private String name;

  @JsonProperty("version")
  @Schema(description = "The version related to the installed application.")
  private String version;

  @JsonProperty("environment")
  @Schema(description = "The environment tag on which the application is installed.")
  private String environment;

  @JsonProperty("db_connection")
  @Schema(description = "The status related to the database connection.", allowableValues = {"up", "down"})
  private String dbConnection;
}
