package it.gov.pagopa.mockconfig.repository;

import it.gov.pagopa.mockconfig.entity.ArchetypeEntity;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchetypeRepository extends JpaRepository<ArchetypeEntity, String> {

    @Override
    Optional<ArchetypeEntity> findById(String archetypeId);

    Optional<ArchetypeEntity> findBySubsystemUrlAndResourceUrlAndHttpMethod(String subsystemUrl, String resourceUrl, HttpMethod httpMethod);

    List<ArchetypeEntity> findBySubsystemUrl(String subsystemUrl);
}