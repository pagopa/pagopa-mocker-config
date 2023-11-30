package it.gov.pagopa.mockconfig.mapper;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.entity.embeddable.InjectableParameterKey;
import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import it.gov.pagopa.mockconfig.model.mockresource.*;
import it.gov.pagopa.mockconfig.util.Utility;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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
                .tags(buildResourceTagEntities(mockResource.getTags()))
                .build();

        List<MockRuleEntity> ruleEntities = new LinkedList<>();
        for (MockRule ruleSource : mockResource.getRules()) {
            MockResponseEntity mockResponseEntity = buildMockResponse(ruleSource.getResponse());
            MockRuleEntity mockRuleEntity = buildMockRule(ruleSource, mockResponseEntity, mockResourceEntity);
            mockRuleEntity.setConditions(buildMockConditions(ruleSource, mockRuleEntity));
            mockResponseEntity.setRule(mockRuleEntity);
            ruleEntities.add(mockRuleEntity);
        }
        ruleEntities.sort(Comparator.comparingInt(MockRuleEntity::getOrder));

        mockResourceEntity.setRules(ruleEntities);

        return mockResourceEntity;
    }

    private List<ResourceTagEntity> buildResourceTagEntities(List<String> tags) {
        List<ResourceTagEntity> tagEntities = new ArrayList<>();
        for (String tag : tags) {
            tagEntities.add(
                    ResourceTagEntity.builder()
                            .id(Utility.generateUUID())
                            .value(tag)
                            .build()
            );
        }
        return tagEntities;
    }

    private List<RuleTagEntity> buildRuleTagEntities(List<String> tags) {
        List<RuleTagEntity> tagEntities = new ArrayList<>();
        for (String tag : tags) {
            tagEntities.add(
                    RuleTagEntity.builder()
                            .id(Utility.generateUUID())
                            .value(tag)
                            .build()
            );
        }
        return tagEntities;
    }

    private MockRuleEntity buildMockRule(MockRule mockRule, MockResponseEntity mockResponseEntity, MockResourceEntity mockResourceEntity) {
        mockRule.setIdIfNull(Utility.generateUUID());
        return MockRuleEntity.builder()
                .id(mockRule.getId())
                .name(mockRule.getName())
                .order(mockRule.getOrder())
                .isActive(mockRule.getIsActive())
                .responseId(mockResponseEntity.getId())
                .response(mockResponseEntity)
                .resourceId(mockResourceEntity.getId())
                .resource(mockResourceEntity)
                .tags(buildRuleTagEntities(mockRule.getTags()))
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
                            .ruleId(mockRule.getId())
                            .rule(mockRuleEntity)
                            .build()
            );
        }
        conditionEntities.sort(Comparator.comparingInt(MockConditionEntity::getOrder));
        return conditionEntities;
    }

    private MockResponseEntity buildMockResponse(MockResponse mockResponse) {
        mockResponse.setIdIfNull(Utility.generateUUID());
        return MockResponseEntity.builder()
                .id(mockResponse.getId())
                .body(mockResponse.getBody())
                .status(mockResponse.getStatus())
                .headers(mockResponse.getHeaders()
                        .stream()
                        .map(header -> ResponseHeaderEntity.builder()
                                .id(ResponseHeaderKey.builder()
                                        .responseId(mockResponse.getId())
                                        .header(header.getName())
                                        .build()
                                )
                                .value(header.getValue())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .parameters(mockResponse.getParameters()
                        .stream()
                        .map(parameter -> InjectableParameterEntity.builder()
                                .id(InjectableParameterKey.builder()
                                        .responseId(mockResponse.getId())
                                        .parameter(parameter)
                                        .build())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }
}
