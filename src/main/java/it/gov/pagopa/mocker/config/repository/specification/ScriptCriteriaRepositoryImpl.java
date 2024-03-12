package it.gov.pagopa.mocker.config.repository.specification;

import it.gov.pagopa.mocker.config.entity.ScriptEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ScriptCriteriaRepositoryImpl implements ScriptCriteriaRepository {


    private final MongoTemplate mongoTemplate;

    public ScriptCriteriaRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ScriptEntity> findAll(String name) {
        Query query = new Query().with(Sort.by(Sort.Direction.ASC, "name"));
        if (StringUtils.isNotEmpty(name)) {
            query.addCriteria(Criteria.where("name").regex(".*" + name + ".*", "i"));
        }
        query.addCriteria(Criteria.where("selectable").is(Boolean.TRUE));
        return mongoTemplate.find(query, ScriptEntity.class);
    }
}
