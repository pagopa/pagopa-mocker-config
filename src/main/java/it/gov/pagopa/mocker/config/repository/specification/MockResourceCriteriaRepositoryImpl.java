package it.gov.pagopa.mocker.config.repository.specification;

import it.gov.pagopa.mocker.config.entity.MockResourceEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MockResourceCriteriaRepositoryImpl implements MockResourceCriteriaRepository {


    private final MongoTemplate mongoTemplate;

    public MockResourceCriteriaRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<MockResourceEntity> findAll(Pageable pageable, String name, String tag) {
        Query query = new Query().with(pageable);
        if (StringUtils.isNotEmpty(name)) {
            query.addCriteria(Criteria.where("name").regex(".*" + name + ".*", "i"));
        }
        if (StringUtils.isNotEmpty(tag)) {
            query.addCriteria(Criteria.where("tags").regex("(" + tag + ")", "i"));

        }
        List<MockResourceEntity> list = mongoTemplate.find(query, MockResourceEntity.class);
        long count = mongoTemplate.count(query, MockResourceEntity.class);
        return new PageImpl<>(list, pageable, count);
    }
}
