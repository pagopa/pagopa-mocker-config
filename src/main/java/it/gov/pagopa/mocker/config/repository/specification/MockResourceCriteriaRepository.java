package it.gov.pagopa.mocker.config.repository.specification;

import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MockResourceCriteriaRepository {

    Page<MockResourceEntity> findAll(Pageable pageable, String name, String tag);
}
