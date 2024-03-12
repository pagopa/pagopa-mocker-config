package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.ScriptEntity;
import it.gov.pagopa.mocker.config.model.script.ScriptMetadata;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class ConvertScriptEntityToScriptMetadata implements Converter<ScriptEntity, ScriptMetadata> {

    @Override
    public ScriptMetadata convert(MappingContext<ScriptEntity, ScriptMetadata> mappingContext) {
        ScriptEntity source = mappingContext.getSource();
        return ScriptMetadata.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .inputParameters(source.getInputParameters())
                .outputParameters(source.getOutputParameters())
                .build();
    }
}
