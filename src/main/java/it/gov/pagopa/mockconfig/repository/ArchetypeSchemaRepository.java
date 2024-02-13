package it.gov.pagopa.mockconfig.repository;

import it.gov.pagopa.mockconfig.entity.ArchetypeSchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchetypeSchemaRepository extends MongoRepository<ArchetypeSchemaEntity, String> {

    Optional<ArchetypeSchemaEntity> findByName(String name);
}
