package it.gov.pagopa.mockconfig.mapper;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.entity.embeddable.InjectableParameterKey;
import it.gov.pagopa.mockconfig.entity.embeddable.ResponseHeaderKey;
import it.gov.pagopa.mockconfig.model.mockresource.MockCondition;
import it.gov.pagopa.mockconfig.model.mockresource.MockResponse;
import it.gov.pagopa.mockconfig.model.mockresource.MockRule;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

public class ConvertMockConditionToMockConditionEntity implements Converter<MockCondition, MockConditionEntity> {

    @Override
    public MockConditionEntity convert(MappingContext<MockCondition, MockConditionEntity> mappingContext) {
        MockCondition source = mappingContext.getSource();
        // mock resource content
        return MockConditionEntity.builder()
                .id(source.getId())
                .order(source.getOrder())
                .fieldPosition(source.getFieldPosition())
                .analyzedContentType(source.getAnalyzedContentType())
                .fieldName(source.getFieldName())
                .conditionType(source.getConditionType())
                .conditionValue(source.getConditionValue())
                .build();
    }
}
