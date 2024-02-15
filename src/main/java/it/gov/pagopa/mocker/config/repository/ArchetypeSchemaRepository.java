package it.gov.pagopa.mocker.config.repository;

import it.gov.pagopa.mocker.config.entity.ArchetypeSchemaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchetypeSchemaRepository extends MongoRepository<ArchetypeSchemaEntity, String> {

    Optional<ArchetypeSchemaEntity> findByName(String name);
}
