package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import it.gov.pagopa.mocker.config.model.mockresource.MockResourceReduced;
import it.gov.pagopa.mocker.config.model.mockresource.SpecialRequestHeader;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;

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
                .specialHeaders(source.getSpecialHeaders().stream()
                        .map(header -> SpecialRequestHeader.builder()
                                .name(header.getName())
                                .value(header.getValue())
                                .build())
                        .toList())
                .httpMethod(source.getHttpMethod())
                .isActive(source.getIsActive())
                .tags(new ArrayList<>(source.getTags()));

        return builder.build();
    }
}
