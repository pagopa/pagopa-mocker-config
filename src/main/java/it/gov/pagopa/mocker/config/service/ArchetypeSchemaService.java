package it.gov.pagopa.mocker.config.service;

import it.gov.pagopa.mocker.config.entity.ArchetypeSchemaEntity;
import it.gov.pagopa.mocker.config.repository.ArchetypeSchemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ArchetypeSchemaService {

    @Autowired private ArchetypeSchemaRepository archetypeSchemaRepository;

    public ArchetypeSchemaEntity validateResponseSchema(ArchetypeSchemaEntity archetypeSchema) {
        ArchetypeSchemaEntity validSchema = null;
        if (archetypeSchema != null) {
            validSchema = archetypeSchemaRepository.findByName(archetypeSchema.getName()).orElseGet(() -> archetypeSchemaRepository.save(archetypeSchema));
        }
        return validSchema;
    }
}
