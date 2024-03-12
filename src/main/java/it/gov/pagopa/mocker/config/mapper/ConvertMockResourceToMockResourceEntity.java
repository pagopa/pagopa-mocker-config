package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.*;
import it.gov.pagopa.mocker.config.model.mockresource.*;
import it.gov.pagopa.mocker.config.util.Utility;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ConvertMockResourceToMockResourceEntity implements Converter<MockResource, MockResourceEntity> {

    @Override
    public MockResourceEntity convert(MappingContext<MockResource, MockResourceEntity> mappingContext) {
        MockResource mockResource = mappingContext.getSource();

        String subsystem = mockResource.getSubsystem().trim();
        String resourceUrl = mockResource.getResourceURL().trim();
        List<NameValueEntity> specialHeaders = mockResource.getSpecialHeaders().stream()
                .map(header -> NameValueEntity.builder()
                        .name(header.getName().trim())
                        .value(header.getValue())
                        .build()
                )
                .toList();

        MockResourceEntity mockResourceEntity = MockResourceEntity.builder()
                .id(Utility.generateResourceId(mockResource.getHttpMethod(), subsystem, resourceUrl, mockResource.getSpecialHeaders()))
                .name(mockResource.getName())
                .subsystemUrl(subsystem)
                .resourceUrl(resourceUrl)
                .specialHeaders(specialHeaders)
                .httpMethod(mockResource.getHttpMethod())
                .isActive(mockResource.getIsActive())
                .tags(new HashSet<>(mockResource.getTags().stream().map(String::trim).filter(value -> !value.isBlank()).toList()))
                .build();

        List<MockRuleEntity> ruleEntities = new LinkedList<>();
        for (MockRule ruleSource : mockResource.getRules()) {
            MockResponseEntity mockResponseEntity = buildMockResponse(ruleSource.getResponse());
            MockRuleEntity mockRuleEntity = buildMockRule(ruleSource, mockResponseEntity);
            mockRuleEntity.setConditions(buildMockConditions(ruleSource));
            ruleEntities.add(mockRuleEntity);
        }
        ruleEntities.sort(Comparator.comparingInt(MockRuleEntity::getOrder));

        mockResourceEntity.setRules(ruleEntities);

        return mockResourceEntity;
    }

    private MockRuleEntity buildMockRule(MockRule mockRule, MockResponseEntity mockResponseEntity) {
        MockScripting mockScripting = mockRule.getScripting();
        ScriptingEntity scriptingEntity = null;
        if (mockScripting != null) {
            scriptingEntity = ScriptingEntity.builder()
                    .scriptName(mockScripting.getScriptName())
                    .isActive(mockScripting.getIsActive())
                    .parameters(mockScripting.getInputParameters().stream()
                            .map(parameter -> NameValueEntity.builder()
                                    .name(parameter.getName())
                                    .value(parameter.getValue())
                                    .build())
                            .toList())
                    .build();
        }

        mockRule.setIdIfNull(Utility.generateUUID());
        return MockRuleEntity.builder()
                .id(mockRule.getId())
                .name(mockRule.getName())
                .order(mockRule.getOrder())
                .isActive(mockRule.getIsActive())
                .response(mockResponseEntity)
                .scripting(scriptingEntity)
                .tags(new HashSet<>(mockRule.getTags().stream().map(String::trim).filter(value -> !value.isBlank()).toList()))
                .build();
    }

    private List<MockConditionEntity> buildMockConditions(MockRule mockRule) {
        List<MockConditionEntity> conditionEntities = new LinkedList<>();
        for (MockCondition mockCondition : mockRule.getConditions()) {
            mockCondition.setIdIfNull(Utility.generateUUID());
            conditionEntities.add(
                    MockConditionEntity.builder()
                            .id(mockCondition.getId())
                            .order(mockCondition.getOrder())
                            .fieldPosition(mockCondition.getFieldPosition())
                            .analyzedContentType(mockCondition.getAnalyzedContentType())
                            .fieldName(mockCondition.getFieldName().trim())
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
                                .header(header.getName().trim())
                                .value(header.getValue())
                                .build()
                        )
                        .toList()
                )
                .parameters(new HashSet<>(mockResponse.getParameters().stream().map(String::trim).filter(value -> !value.isBlank()).toList()))
                .build();
    }
}
