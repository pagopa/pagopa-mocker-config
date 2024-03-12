package it.gov.pagopa.mocker.config.repository.specification;

import it.gov.pagopa.mocker.config.entity.ScriptEntity;

import java.util.List;

public interface ScriptCriteriaRepository {

    List<ScriptEntity> findAll(String name);
}
