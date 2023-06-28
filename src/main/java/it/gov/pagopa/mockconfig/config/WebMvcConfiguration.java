package it.gov.pagopa.mockconfig.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.mockconfig.model.AppCorsConfiguration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Value("${cors.configuration:{}}")
  private String corsConfiguration;

  @SneakyThrows
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    AppCorsConfiguration appCorsConfiguration = Optional.ofNullable(
            new ObjectMapper().readValue(corsConfiguration, AppCorsConfiguration.class)
    ).orElse(new AppCorsConfiguration());
    registry.addMapping("/**")
        .allowedOrigins(Optional.ofNullable(appCorsConfiguration.getOrigins()).orElse(new String[]{}))
        .allowedMethods(Optional.ofNullable(appCorsConfiguration.getMethods()).orElse(new String[]{}));
  }
}


