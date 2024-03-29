package it.gov.pagopa.mocker.config.repository;

import it.gov.pagopa.mocker.config.entity.ArchetypeEntity;
import it.gov.pagopa.mocker.config.model.enumeration.HttpMethod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchetypeRepository extends MongoRepository<ArchetypeEntity, String> {

    @Override
    Optional<ArchetypeEntity> findById(String archetypeId);

    Optional<ArchetypeEntity> findBySubsystemUrlAndResourceUrlAndHttpMethod(String subsystemUrl, String resourceUrl, HttpMethod httpMethod);

    List<ArchetypeEntity> findBySubsystemUrl(String subsystemUrl);
}