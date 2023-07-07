package it.gov.pagopa.mockconfig.service;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.exception.AppError;
import it.gov.pagopa.mockconfig.exception.AppException;
import it.gov.pagopa.mockconfig.model.enumeration.RuleFieldPosition;
import it.gov.pagopa.mockconfig.model.mockresource.*;
import it.gov.pagopa.mockconfig.repository.MockResourceRepository;
import it.gov.pagopa.mockconfig.repository.TagRepository;
import it.gov.pagopa.mockconfig.util.Constants;
import it.gov.pagopa.mockconfig.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class MockResourceService {

    @Autowired private MockResourceRepository mockResourceRepository;

    @Autowired private TagRepository tagRepository;

    @Autowired private ModelMapper modelMapper;

    public MockResourceList getMockResources(Pageable pageable) {
        List<MockResource> mockResources;
        Page<MockResourceEntity> mockResourcePaginatedEntities;
        try {
            mockResourcePaginatedEntities = mockResourceRepository.findAll(pageable);
            mockResources = mockResourcePaginatedEntities.stream()
                    .map(mockResource -> modelMapper.map(mockResource, MockResource.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to retrieve a list of mock resources. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return MockResourceList.builder()
                .mockResources(mockResources)
                .pageInfo(Utility.buildPageInfo(mockResourcePaginatedEntities))
                .build();
    }

    public MockResource getMockResource(String resourceId) {
        MockResourceEntity mockResourceEntity;
        try {
            mockResourceEntity = mockResourceRepository.findById(resourceId)
                    .orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, resourceId));
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to retrieve the detail of a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return modelMapper.map(mockResourceEntity, MockResource.class);
    }

    public MockResource createMockResource(MockResource mockResource) {
        MockResource response;
        try {

            // check request semantic validity
            checkRequestSemanticValidity(mockResource);

            // Search if the resource already exists
            String resourceId = Utility.generateResourceId(mockResource);
            mockResourceRepository.findByResourceId(resourceId).ifPresent(res -> {throw new AppException(AppError.MOCK_RESOURCE_CONFLICT, resourceId); });

            // Persisting the mock resource
            response = persistMockResource(mockResource);

        } catch (DataAccessException e) {
            log.error("An error occurred while trying to create a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MockResource updateMockResource(String resourceId, MockResource mockResource) {
        MockResource response;
        try {

            // check request semantic validity
            checkRequestSemanticValidity(mockResource);

            // Search if the resource exists
            String generatedResourceId = Utility.generateResourceId(mockResource);
            MockResourceEntity mockResourceEntity = mockResourceRepository.findByResourceId(generatedResourceId).orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, resourceId));

            // Check if passed resource identifier is equals to the one generable by body content
            if (!mockResourceEntity.getId().equals(resourceId)) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_ID, resourceId, mockResourceEntity.getId());
            }

            if (!mockResourceEntity.getResourceUrl().equals(mockResource.getResourceURL()) || !mockResourceEntity.getSubsystemUrl().equals(mockResource.getSubsystem())) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_URL, resourceId, mockResourceEntity.getId());
            }

            // delete the old resource, the new one will replace this resource
            mockResourceRepository.delete(mockResourceEntity);

            // Persisting the mock resource
            response = persistMockResource(mockResource);

        } catch (DataAccessException e) {
            log.error("An error occurred while trying to update a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public void deleteMockResource(String resourceId) {
        try {
            MockResourceEntity mockResourceEntity = mockResourceRepository.findById(resourceId)
                    .orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, resourceId));
            mockResourceRepository.delete(mockResourceEntity);
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to delete a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
    }

    public void importMockResourcesFromOpenAPI() {

    }

    private void checkRequestSemanticValidity(MockResource mockResource) {

        Set<Integer> assignedRuleOrderCardinality = new HashSet<>();
        for (MockRule mockRule : mockResource.getRules()) {

            String mockRuleName = mockRule.getName();

            // check if there is a duplicate value on rule order value
            int ruleOrder = mockRule.getOrder();
            if (assignedRuleOrderCardinality.contains(ruleOrder)) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_DUPLICATE_RULE_ORDER);
            }
            assignedRuleOrderCardinality.add(ruleOrder);

            Set<Integer> assignedConditionOrderCardinality = new HashSet<>();
            for (MockCondition mockCondition : mockRule.getConditions()) {

                // check if there is a duplicate value on condition order value
                int conditionOrder = mockCondition.getOrder();
                if (assignedConditionOrderCardinality.contains(conditionOrder)) {
                    throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_DUPLICATE_CONDITION_ORDER, mockRuleName);
                }
                assignedConditionOrderCardinality.add(ruleOrder);

                // check if the content type JSON,XML will be evaluated as other than body
                if (Constants.CONTENT_TYPES_FOR_BODY.contains(mockCondition.getAnalyzedContentType()) && !RuleFieldPosition.BODY.equals(mockCondition.getFieldPosition())) {
                    throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_CONTENT_TYPE, mockRuleName, conditionOrder, mockCondition.getAnalyzedContentType(), mockCondition.getFieldPosition());
                }

                // check if there aren't the following cases: condition_value=null in non-nullable condition, condition_value=non-null in unary condition
                if (mockCondition.getConditionValue() != null && Constants.UNARY_CONDITIONS.contains(mockCondition.getConditionType())) {
                    throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_UNARY_CONDITION, mockRuleName, conditionOrder);
                } else if (mockCondition.getConditionValue() == null && !Constants.UNARY_CONDITIONS.contains(mockCondition.getConditionType())) {
                    throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_BINARY_CONDITION, mockRuleName, conditionOrder);
                }
            }

            // check if the body response is a valid Base64 content
            try {
                MockResponse mockResponse = mockRule.getResponse();
                Base64.getDecoder().decode(mockResponse.getBody());
            } catch (IllegalArgumentException e) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_UNPARSEABLE_RESPONSE_BODY, mockRuleName);
            }
        }
    }

    private MockResource persistMockResource(MockResource mockResource) {
        // Map entity from input model, setting id and tags and completing the entities' tree
        MockResourceEntity mockResourceEntity = modelMapper.map(mockResource, MockResourceEntity.class);
        mockResourceEntity.setTags(validateTags(mockResourceEntity.getTags()));
        mockResourceEntity.getRules().forEach(rule -> rule.setTags(validateTags(rule.getTags())));

        // Save the converted resource
        mockResourceEntity = mockResourceRepository.save(mockResourceEntity);
        return modelMapper.map(mockResourceEntity, MockResource.class);
    }

    private List<TagEntity> validateTags(List<TagEntity> tagEntities) {
        List<TagEntity> validTags = new LinkedList<>();
        for (TagEntity tagEntity : tagEntities) {
            validTags.add(tagRepository.findByValue(tagEntity.getValue()).orElseGet(() -> tagRepository.save(tagEntity)));
        }
        return validTags;
    }
}
