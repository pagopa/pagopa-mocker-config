package it.gov.pagopa.mockconfig.mapper;

import it.gov.pagopa.mockconfig.entity.InjectableParameterEntity;
import it.gov.pagopa.mockconfig.entity.MockResponseEntity;
import it.gov.pagopa.mockconfig.entity.MockRuleEntity;
import it.gov.pagopa.mockconfig.entity.ResponseHeaderEntity;
import it.gov.pagopa.mockconfig.entity.embeddable.InjectableParameterKey;
import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import it.gov.pagopa.mockconfig.model.mockresource.MockResponse;
import it.gov.pagopa.mockconfig.model.mockresource.MockRule;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

public class ConvertMockRuleToMockRuleEntity implements Converter<MockRule, MockRuleEntity> {

    @Override
    public MockRuleEntity convert(MappingContext<MockRule, MockRuleEntity> mappingContext) {
        MockRule ruleSource = mappingContext.getSource();
        // mock resource content
        MockResponseEntity mockResponseEntity = buildResponse(ruleSource.getResponse());
        MockRuleEntity mockRuleEntity = MockRuleEntity.builder()
                .id(ruleSource.getId())
                .name(ruleSource.getName())
                .order(ruleSource.getOrder())
                .isActive(ruleSource.getIsActive())
                .response(mockResponseEntity)
                .build();
        mockResponseEntity.setRule(mockRuleEntity);
        mockResponseEntity.getParameters().forEach(parameter -> parameter.setResponse(mockResponseEntity));
        return mockRuleEntity;
    }

    private MockResponseEntity buildResponse(MockResponse responseSource) {
        return MockResponseEntity.builder()
                .id(responseSource.getId())
                .body(responseSource.getBody())
                .status(responseSource.getStatus())
                .headers(responseSource.getHeaders()
                        .stream()
                        .map(header -> ResponseHeaderEntity.builder()
                                .id(ResponseHeaderKey.builder()
                                        .responseId(responseSource.getId())
                                        .header(header.getName())
                                        .build()
                                )
                                .value(header.getValue())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .parameters(responseSource.getParameters()
                        .stream()
                        .map(parameter -> InjectableParameterEntity.builder()
                                .id(InjectableParameterKey.builder()
                                        .responseId(responseSource.getId())
                                        .parameter(parameter)
                                        .build())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }
}
