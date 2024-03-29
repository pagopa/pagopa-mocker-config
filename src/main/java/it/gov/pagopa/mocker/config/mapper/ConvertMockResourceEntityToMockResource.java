package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.*;
import it.gov.pagopa.mocker.config.model.mockresource.*;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
                    .headers(srcResponse.getHeaders().stream()
                            .map(header -> ResponseHeader.builder()
                                    .name(header.getHeader())
                                    .value(header.getValue())
                                    .build())
                            .toList())
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

            ScriptingEntity scriptingEntity = srcRule.getScripting();
            MockScripting mockScripting = null;
            if (scriptingEntity != null) {
                mockScripting = MockScripting.builder()
                        .scriptName(scriptingEntity.getScriptName())
                        .isActive(scriptingEntity.getIsActive())
                        .inputParameters(scriptingEntity.getParameters().stream()
                                .map(parameter -> ScriptParameter.builder()
                                        .name(parameter.getName())
                                        .value(parameter.getValue())
                                        .build())
                                .toList())
                        .build();
            }

            MockRule rule = MockRule.builder()
                    .id(srcRule.getId())
                    .name(srcRule.getName())
                    .order(srcRule.getOrder())
                    .isActive(srcRule.isActive())
                    .tags(new ArrayList<>(srcRule.getTags()))
                    .conditions(conditions)
                    .scripting(mockScripting)
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
                .specialHeaders(source.getSpecialHeaders().stream()
                        .map(header -> SpecialRequestHeader.builder()
                                .name(header.getName())
                                .value(header.getValue())
                                .build())
                        .toList())
                .httpMethod(source.getHttpMethod())
                .isActive(source.getIsActive())
                .tags(new ArrayList<>(source.getTags()))
                .rules(rules);

        return builder.build();
    }
}
