package it.gov.pagopa.mocker.config.repository;

import it.gov.pagopa.mocker.config.entity.ScriptEntity;
import it.gov.pagopa.mocker.config.repository.specification.ScriptCriteriaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScriptRepository extends MongoRepository<ScriptEntity, String>, ScriptCriteriaRepository {

    Optional<ScriptEntity> findByName(String name);
}