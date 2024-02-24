package it.gov.pagopa.mocker.config.repository;

import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import it.gov.pagopa.mocker.config.repository.specification.MockResourceCriteriaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MockResourceRepository extends MongoRepository<MockResourceEntity, String>, MockResourceCriteriaRepository {

    Optional<MockResourceEntity> findById(String id);
}