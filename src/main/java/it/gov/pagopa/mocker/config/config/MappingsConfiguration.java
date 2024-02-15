package it.gov.pagopa.mocker.config.config;

import it.gov.pagopa.mocker.config.entity.ArchetypeEntity;
import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import it.gov.pagopa.mocker.config.entity.MockRuleEntity;
import it.gov.pagopa.mocker.config.mapper.*;
import it.gov.pagopa.mocker.config.model.archetype.Archetype;
import it.gov.pagopa.mocker.config.model.mockresource.MockResource;
import it.gov.pagopa.mocker.config.model.mockresource.MockResourceReduced;
import it.gov.pagopa.mocker.config.model.mockresource.MockRule;
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
    mapper.createTypeMap(MockResourceEntity.class, MockResourceReduced.class).setConverter(new ConvertMockResourceEntityToMockResourceReduced());
    mapper.createTypeMap(MockResource.class, MockResourceEntity.class).setConverter(new ConvertMockResourceToMockResourceEntity());
    mapper.createTypeMap(MockRule.class, MockRuleEntity.class).setConverter(new ConvertMockRuleToMockRuleEntity());
    mapper.createTypeMap(ArchetypeEntity.class, Archetype.class).setConverter(new ConvertArchetypeEntityToArchetype());

    //
    return mapper;
  }
}
