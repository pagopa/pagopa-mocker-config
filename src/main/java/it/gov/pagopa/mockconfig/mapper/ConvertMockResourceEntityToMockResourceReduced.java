package it.gov.pagopa.mockconfig.mapper;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.model.mockresource.*;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConvertMockResourceEntityToMockResourceReduced implements Converter<MockResourceEntity, MockResourceReduced> {

    @Override
    public MockResourceReduced convert(MappingContext<MockResourceEntity, MockResourceReduced> mappingContext) {
        MockResourceEntity source = mappingContext.getSource();
        MockResourceReduced.MockResourceReducedBuilder builder = MockResourceReduced.builder();

        // mock resource content
        builder.id(source.getId())
                .name(source.getName())
                .subsystem(source.getSubsystemUrl())
                .resourceURL(source.getResourceUrl())
                .soapAction(source.getAction())
                .httpMethod(source.getHttpMethod())
                .isActive(source.getIsActive())
                .tags(Optional.ofNullable(source.getTags()).orElse(List.of()).stream().map(ResourceTagEntity::getValue).collect(Collectors.toList()));

        return builder.build();
    }
}
