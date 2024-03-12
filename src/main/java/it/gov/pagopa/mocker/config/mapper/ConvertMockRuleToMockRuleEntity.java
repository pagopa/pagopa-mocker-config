package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.*;
import it.gov.pagopa.mocker.config.model.mockresource.MockCondition;
import it.gov.pagopa.mocker.config.model.mockresource.MockResponse;
import it.gov.pagopa.mocker.config.model.mockresource.MockRule;
import it.gov.pagopa.mocker.config.model.mockresource.MockScripting;
import it.gov.pagopa.mocker.config.util.Utility;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ConvertMockRuleToMockRuleEntity implements Converter<MockRule, MockRuleEntity> {

    @Override
    public MockRuleEntity convert(MappingContext<MockRule, MockRuleEntity> mappingContext) {
        MockRule mockRule = mappingContext.getSource();

        MockResponseEntity mockResponseEntity = buildMockResponse(mockRule.getResponse());
        MockRuleEntity mockRuleEntity = buildMockRule(mockRule, mockResponseEntity);
        mockRuleEntity.setConditions(buildMockConditions(mockRule, mockRuleEntity));

        return mockRuleEntity;
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
