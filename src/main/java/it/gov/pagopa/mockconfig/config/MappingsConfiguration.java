package it.gov.pagopa.mockconfig.config;

import it.gov.pagopa.mockconfig.entity.MockConditionEntity;
import it.gov.pagopa.mockconfig.entity.MockResourceEntity;
import it.gov.pagopa.mockconfig.entity.MockRuleEntity;
import it.gov.pagopa.mockconfig.mapper.ConvertMockConditionToMockConditionEntity;
import it.gov.pagopa.mockconfig.mapper.ConvertMockResourceEntityToMockResource;
import it.gov.pagopa.mockconfig.mapper.ConvertMockResourceToMockResourceEntity;
import it.gov.pagopa.mockconfig.mapper.ConvertMockRuleToMockRuleEntity;
import it.gov.pagopa.mockconfig.model.mockresource.MockCondition;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import it.gov.pagopa.mockconfig.model.mockresource.MockRule;
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
    mapper.createTypeMap(MockRule.class, MockRuleEntity.class).setConverter(new ConvertMockRuleToMockRuleEntity());
    mapper.createTypeMap(MockCondition.class, MockConditionEntity.class).setConverter(new ConvertMockConditionToMockConditionEntity());

    return mapper;
  }
}
