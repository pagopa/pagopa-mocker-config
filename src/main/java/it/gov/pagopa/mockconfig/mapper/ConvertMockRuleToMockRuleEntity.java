package it.gov.pagopa.mockconfig.mapper;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.model.mockresource.MockCondition;
import it.gov.pagopa.mockconfig.model.mockresource.MockResponse;
import it.gov.pagopa.mockconfig.model.mockresource.MockRule;
import it.gov.pagopa.mockconfig.util.Utility;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.*;
import java.util.stream.Collectors;

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
