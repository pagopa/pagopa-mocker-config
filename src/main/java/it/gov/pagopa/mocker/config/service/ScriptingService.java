package it.gov.pagopa.mocker.config.service;

import it.gov.pagopa.mocker.config.entity.ScriptEntity;
import it.gov.pagopa.mocker.config.exception.AppError;
import it.gov.pagopa.mocker.config.exception.AppException;
import it.gov.pagopa.mocker.config.model.script.ScriptMetadata;
import it.gov.pagopa.mocker.config.model.script.ScriptMetadataList;
import it.gov.pagopa.mocker.config.repository.ScriptRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScriptingService {

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ScriptMetadataList getScripts(String name) {
        List<ScriptMetadata> scripts;
        try {
            List<ScriptEntity> scriptEntities = scriptRepository.findAll(name);
            scripts = scriptEntities.stream()
                    .map(script -> modelMapper.map(script, ScriptMetadata.class))
                    .toList();
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to retrieve the list of scripts. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return ScriptMetadataList.builder()
                .scripts(scripts)
                .build();
    }
}
