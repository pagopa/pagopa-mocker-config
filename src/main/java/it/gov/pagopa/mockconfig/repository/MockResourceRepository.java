package it.gov.pagopa.mockconfig.repository;

import it.gov.pagopa.mockconfig.entity.MockResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MockResourceRepository extends JpaRepository<MockResourceEntity, String> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"tags"})
    Page<MockResourceEntity> findAll(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"tags"})
    Optional<MockResourceEntity> findById(String id);
}