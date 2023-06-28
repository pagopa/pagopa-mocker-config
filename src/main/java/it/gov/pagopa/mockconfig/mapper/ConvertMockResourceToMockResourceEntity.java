package it.gov.pagopa.mockconfig.mapper;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.model.mockresource.*;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertMockResourceToMockResourceEntity implements Converter<MockResource, MockResourceEntity> {

    @Override
    public MockResourceEntity convert(MappingContext<MockResource, MockResourceEntity> mappingContext) {
        MockResource source = mappingContext.getSource();
        return MockResourceEntity.builder()
                .id(source.getId())
                .name(source.getName())
                .subsystemUrl(source.getSubsystem())
                .resourceUrl(source.getResourceURL())
                .httpMethod(source.getHttpMethod())
                .isActive(source.getIsActive())
                .build();
    }
}
