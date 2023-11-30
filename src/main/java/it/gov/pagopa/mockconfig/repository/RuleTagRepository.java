package it.gov.pagopa.mockconfig.repository;

import it.gov.pagopa.mockconfig.entity.RuleTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleTagRepository extends JpaRepository<RuleTagEntity, String> {

    Optional<RuleTagEntity> findByValue(String value);
}