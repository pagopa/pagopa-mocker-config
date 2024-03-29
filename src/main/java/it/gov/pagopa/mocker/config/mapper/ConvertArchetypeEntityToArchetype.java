package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.model.archetype.Archetype;
import it.gov.pagopa.mocker.config.model.archetype.ArchetypeResponse;
import it.gov.pagopa.mocker.config.model.archetype.ArchetypeResponseHeader;
import it.gov.pagopa.mocker.config.model.enumeration.ArchetypeParameterType;
import it.gov.pagopa.mocker.config.entity.ArchetypeEntity;
import it.gov.pagopa.mocker.config.entity.ArchetypeParameterEntity;
import it.gov.pagopa.mocker.config.entity.ArchetypeResponseEntity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.*;
import java.util.stream.Collectors;

public class ConvertArchetypeEntityToArchetype implements Converter<ArchetypeEntity, Archetype> {

    @Override
    public Archetype convert(MappingContext<ArchetypeEntity, Archetype> mappingContext) {
        ArchetypeEntity source = mappingContext.getSource();

        List<ArchetypeResponse> responses = new LinkedList<>();
        for (ArchetypeResponseEntity archetypeResponseEntity : source.getResponses()) {
            //String body = archetypeResponseEntity.getSchema() != null ? archetypeResponseEntity.getSchema().getContent() : null;
            responses.add(ArchetypeResponse.builder()
                    .id(archetypeResponseEntity.getId())
                    //.body(body)
                    .status(archetypeResponseEntity.getStatus())
                    .headers(archetypeResponseEntity.getHeaders().stream()
                            .map(archetypeResponseHeaderEntity -> ArchetypeResponseHeader.builder()
                                    .name(archetypeResponseHeaderEntity.getHeader())
                                    .value(archetypeResponseHeaderEntity.getValue())
                                    .build())
                            .collect(Collectors.toList())
                    )
                    //.injectableParameters(body == null || !MockResourceValidation.checkBodyEncoding(body) ? List.of() : Utility.extractInjectableParameters(new String(Base64.getDecoder().decode(body.getBytes()))))
                    .build()
            );
        }

        return Archetype.builder()
                .id(source.getId())
                .name(source.getName())
                .subsystem(source.getSubsystemUrl())
                .resourceURL(source.getResourceUrl())
                .httpMethod(source.getHttpMethod())
                .tags(source.getTags())
                .urlParameters(Optional.ofNullable(source.getParameters())
                        .orElse(List.of())
                        .stream()
                        .filter(archetypeParameterEntity -> ArchetypeParameterType.PATH.equals(archetypeParameterEntity.getType()))
                        .map(ArchetypeParameterEntity::getName)
                        .collect(Collectors.toList()))
                .responses(responses)
                .build();
    }
}
