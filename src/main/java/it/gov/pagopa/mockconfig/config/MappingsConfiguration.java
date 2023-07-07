package it.gov.pagopa.mockconfig.config;

import it.gov.pagopa.mockconfig.entity.MockResourceEntity;
import it.gov.pagopa.mockconfig.mapper.ConvertMockResourceEntityToMockResource;
import it.gov.pagopa.mockconfig.mapper.ConvertMockResourceToMockResourceEntity;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingsConfiguration {

  @Bean
  ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    // insert here the new mappers
    mapper.createTypeMap(MockResourceEntity.class, MockResource.class).setConverter(new ConvertMockResourceEntityToMockResource());
    mapper.createTypeMap(MockResource.class, MockResourceEntity.class).setConverter(new ConvertMockResourceToMockResourceEntity());

    return mapper;
  }
}
