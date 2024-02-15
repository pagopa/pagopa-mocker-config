package it.gov.pagopa.mocker.config.repository;

import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MockResourceRepository extends MongoRepository<MockResourceEntity, String> {


    Page<MockResourceEntity> findAll(Pageable pageable);

    Optional<MockResourceEntity> findById(String id);
}