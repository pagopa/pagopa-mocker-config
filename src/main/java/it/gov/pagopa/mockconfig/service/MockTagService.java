package it.gov.pagopa.mockconfig.service;

import it.gov.pagopa.mockconfig.entity.ResourceTagEntity;
import it.gov.pagopa.mockconfig.entity.RuleTagEntity;
import it.gov.pagopa.mockconfig.repository.ResourceTagRepository;
import it.gov.pagopa.mockconfig.repository.RuleTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class MockTagService {

    @Autowired private ResourceTagRepository resourceTagRepository;

    @Autowired private RuleTagRepository ruleTagRepository;

    public List<RuleTagEntity> validateRuleTags(List<RuleTagEntity> tagEntities) {
        List<RuleTagEntity> validTags = new LinkedList<>();
        if (tagEntities != null) {
            for (RuleTagEntity tagEntity : tagEntities) {
                validTags.add(ruleTagRepository.findByValue(tagEntity.getValue()).orElseGet(() -> ruleTagRepository.save(tagEntity)));
            }
        }
        return validTags;
    }

    public List<ResourceTagEntity> validateResourceTags(List<ResourceTagEntity> tagEntities) {
        List<ResourceTagEntity> validTags = new LinkedList<>();
        if (tagEntities != null) {
            for (ResourceTagEntity tagEntity : tagEntities) {
                validTags.add(resourceTagRepository.findByValue(tagEntity.getValue()).orElseGet(() -> resourceTagRepository.save(tagEntity)));
            }
        }
        return validTags;
    }
}
