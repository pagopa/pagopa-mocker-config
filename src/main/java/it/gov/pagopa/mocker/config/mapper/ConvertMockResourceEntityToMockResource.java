package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.MockConditionEntity;
import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import it.gov.pagopa.mocker.config.entity.MockResponseEntity;
import it.gov.pagopa.mocker.config.entity.MockRuleEntity;
import it.gov.pagopa.mocker.config.model.mockresource.*;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertMockResourceEntityToMockResource implements Converter<MockResourceEntity, MockResource> {

    @Override
    public MockResource convert(MappingContext<MockResourceEntity, MockResource> mappingContext) {
        MockResourceEntity source = mappingContext.getSource();
        MockResource.MockResourceBuilder builder = MockResource.builder();

        // mock rule contents
        List<MockRule> rules = new ArrayList<>();
        for (MockRuleEntity srcRule : source.getRules()) {

            MockResponseEntity srcResponse = srcRule.getResponse();
            MockResponse response = MockResponse.builder()
                    .body(srcResponse.getBody())
                    .status(srcResponse.getStatus())
                    .parameters(new ArrayList<>(srcResponse.getParameters()))
                    .headers(srcResponse.getHeaders().stream().map(header -> ResponseHeader.builder().name(header.getHeader()).value(header.getValue()).build()
                    ).collect(Collectors.toList()))
                    .build();

            List<MockCondition> conditions = new ArrayList<>();
            for (MockConditionEntity srcCondition : srcRule.getConditions()) {
                conditions.add(
                        MockCondition.builder()
                                .id(srcCondition.getId())
                                .order(srcCondition.getOrder())
                                .analyzedContentType(srcCondition.getAnalyzedContentType())
                                .conditionValue(srcCondition.getConditionValue())
                                .conditionType(srcCondition.getConditionType())
                                .fieldPosition(srcCondition.getFieldPosition())
                                .fieldName(srcCondition.getFieldName())
                                .build()
                );
            }
            conditions.sort(Comparator.comparingInt(MockCondition::getOrder));

            MockRule rule = MockRule.builder()
                    .id(srcRule.getId())
                    .name(srcRule.getName())
                    .order(srcRule.getOrder())
                    .isActive(srcRule.isActive())
                    .tags(new ArrayList<>(srcRule.getTags()))
                    .conditions(conditions)
                    .response(response)
                    .build();
            rules.add(rule);
        }
        rules.sort(Comparator.comparingInt(MockRule::getOrder));

        // mock resource content
        builder.id(source.getId())
                .name(source.getName())
                .subsystem(source.getSubsystemUrl())
                .resourceURL(source.getResourceUrl())
                .soapAction(source.getAction())
                .httpMethod(source.getHttpMethod())
                .isActive(source.getIsActive())
                .tags(new ArrayList<>(source.getTags()))
                .rules(rules);

        return builder.build();
    }
}
