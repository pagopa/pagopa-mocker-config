package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.*;
import it.gov.pagopa.mocker.config.util.Utility;
import it.gov.pagopa.mocker.config.model.mockresource.MockCondition;
import it.gov.pagopa.mocker.config.model.mockresource.MockResource;
import it.gov.pagopa.mocker.config.model.mockresource.MockResponse;
import it.gov.pagopa.mocker.config.model.mockresource.MockRule;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.*;
import java.util.stream.Collectors;

public class ConvertMockResourceToMockResourceEntity implements Converter<MockResource, MockResourceEntity> {

    @Override
    public MockResourceEntity convert(MappingContext<MockResource, MockResourceEntity> mappingContext) {
        MockResource mockResource = mappingContext.getSource();

        MockResourceEntity mockResourceEntity = MockResourceEntity.builder()
                .id(Utility.generateResourceId(mockResource.getHttpMethod(), mockResource.getSubsystem(), mockResource.getResourceURL(), mockResource.getSoapAction()))
                .name(mockResource.getName())
                .subsystemUrl(mockResource.getSubsystem())
                .resourceUrl(mockResource.getResourceURL())
                .action(mockResource.getSoapAction())
                .httpMethod(mockResource.getHttpMethod())
                .isActive(mockResource.getIsActive())
                .tags(new HashSet<>(mockResource.getTags()))
                .build();

        List<MockRuleEntity> ruleEntities = new LinkedList<>();
        for (MockRule ruleSource : mockResource.getRules()) {
            MockResponseEntity mockResponseEntity = buildMockResponse(ruleSource.getResponse());
            MockRuleEntity mockRuleEntity = buildMockRule(ruleSource, mockResponseEntity, mockResourceEntity);
            mockRuleEntity.setConditions(buildMockConditions(ruleSource, mockRuleEntity));
            ruleEntities.add(mockRuleEntity);
        }
        ruleEntities.sort(Comparator.comparingInt(MockRuleEntity::getOrder));

        mockResourceEntity.setRules(ruleEntities);

        return mockResourceEntity;
    }

    private MockRuleEntity buildMockRule(MockRule mockRule, MockResponseEntity mockResponseEntity, MockResourceEntity mockResourceEntity) {
        mockRule.setIdIfNull(Utility.generateUUID());
        return MockRuleEntity.builder()
                .id(mockRule.getId())
                .name(mockRule.getName())
                .order(mockRule.getOrder())
                .isActive(mockRule.getIsActive())
                .response(mockResponseEntity)
                .tags(new HashSet<>(mockRule.getTags()))
                .build();
    }

    private List<MockConditionEntity> buildMockConditions(MockRule mockRule, MockRuleEntity mockRuleEntity) {
        List<MockConditionEntity> conditionEntities = new LinkedList<>();
        for (MockCondition mockCondition : mockRule.getConditions()) {
            mockCondition.setIdIfNull(Utility.generateUUID());
            conditionEntities.add(
                    MockConditionEntity.builder()
                            .id(mockCondition.getId())
                            .order(mockCondition.getOrder())
                            .fieldPosition(mockCondition.getFieldPosition())
                            .analyzedContentType(mockCondition.getAnalyzedContentType())
                            .fieldName(mockCondition.getFieldName())
                            .conditionType(mockCondition.getConditionType())
                            .conditionValue(mockCondition.getConditionValue())
                            .build()
            );
        }
        conditionEntities.sort(Comparator.comparingInt(MockConditionEntity::getOrder));
        return conditionEntities;
    }

    private MockResponseEntity buildMockResponse(MockResponse mockResponse) {
        mockResponse.setIdIfNull(Utility.generateUUID());
        return MockResponseEntity.builder()
                .body(mockResponse.getBody())
                .status(mockResponse.getStatus())
                .headers(mockResponse.getHeaders()
                        .stream()
                        .map(header -> ResponseHeaderEntity.builder()
                                .header(header.getName())
                                .value(header.getValue())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .parameters(new HashSet<>(mockResponse.getParameters()))
                .build();
    }
}
