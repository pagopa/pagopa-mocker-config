package it.gov.pagopa.mockconfig.repository;

import it.gov.pagopa.mockconfig.entity.MockResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MockResourceRepository extends JpaRepository<MockResourceEntity, String> {

    Page<MockResourceEntity> findAll(Pageable pageable);

    Optional<MockResourceEntity> findById(Long id);
}