package it.gov.pagopa.mockconfig.service;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.exception.AppError;
import it.gov.pagopa.mockconfig.exception.AppException;
import it.gov.pagopa.mockconfig.model.mockresource.MockCondition;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import it.gov.pagopa.mockconfig.model.mockresource.MockResourceList;
import it.gov.pagopa.mockconfig.model.mockresource.MockRule;
import it.gov.pagopa.mockconfig.repository.MockResourceRepository;
import it.gov.pagopa.mockconfig.repository.TagRepository;
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

            // Search if the resource already exists
            String resourceId = Utility.generateResourceId(mockResource);
            mockResourceRepository.findById(resourceId).ifPresent(res -> {throw new AppException(AppError.MOCK_RESOURCE_CONFLICT, resourceId); });

            // Map entity from input model, setting id and tags and completing the entities' tree
            MockResourceEntity mockResourceEntity = modelMapper.map(mockResource, MockResourceEntity.class);
            mockResourceEntity.setId(resourceId);
            mockResourceEntity.setTags(getTagEntities(mockResource.getTags()));
            extractMockRuleEntities(mockResource, mockResourceEntity);

            // Save the converted resource
            mockResourceEntity = mockResourceRepository.save(mockResourceEntity);
            response = modelMapper.map(mockResourceEntity, MockResource.class);

        } catch (DataAccessException e) {
            log.error("An error occurred while trying to create a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MockResource updateMockResource(String resourceId, MockResource mockResource) {
        MockResource response;
        try {

            // Check if passed resource identifier is equals to the one generable by body content
            String generatedResourceId = Utility.generateResourceId(mockResource);
            if (!generatedResourceId.equals(resourceId)) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_ID, resourceId, generatedResourceId);
            }

            // Search if the resource exists
            MockResourceEntity mockResourceEntity = mockResourceRepository.findById(resourceId).orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, resourceId));

            // Map entity from input model, setting id and tags and completing the entities' tree
            mockResourceEntity.setName(mockResource.getName());
            mockResourceEntity.setIsActive(mockResource.getIsActive());
            mockResourceEntity.setTags(getTagEntities(mockResource.getTags()));
            extractMockRuleEntities(mockResource, mockResourceEntity);

            // Save the converted resource
            mockResourceEntity = mockResourceRepository.save(mockResourceEntity);
            response = modelMapper.map(mockResourceEntity, MockResource.class);

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


    private MockResourceEntity extractMockRuleEntities(MockResource mockResource, MockResourceEntity mockResourceEntity) {
        List<MockRuleEntity> mockRuleEntities = new LinkedList<>();

        // Remove old rules that does not exists in the new configuration
        if (mockResourceEntity.getRules() == null) {
            mockResourceEntity.setRules(new LinkedList<>());
        }
        mockResourceEntity.getRules().clear();

        for (MockRule mockRule : mockResource.getRules()) {

            // Generating the UUIDs for the new object to be generated
            mockRule.setIdIfNull(Utility.generateUUID());
            mockRule.getResponse().setIdIfNull(Utility.generateUUID());

            // Map a rule entity from the passed model, then generate its conditions and assign them to rule
            MockRuleEntity mockRuleEntity = modelMapper.map(mockRule, MockRuleEntity.class);
            List<MockConditionEntity> mockConditionEntities = new ArrayList<>();
            mockRuleEntity.setResource(mockResourceEntity);
            mockRuleEntity.setConditions(mockConditionEntities);
            mockRuleEntity.setTags(getTagEntities(mockRule.getTags()));

            // Generate each condition to be added to rule
            for (MockCondition mockCondition : mockRule.getConditions()) {
                mockCondition.setIdIfNull(Utility.generateUUID());
                MockConditionEntity mockConditionEntity = modelMapper.map(mockCondition, MockConditionEntity.class);
                mockConditionEntity.setRule(mockRuleEntity);
                mockConditionEntities.add(mockConditionEntity);
            }
            mockConditionEntities.sort(Comparator.comparingInt(MockConditionEntity::getOrder));

            // Add the generated rule to the list of all rules
            mockRuleEntities.add(mockRuleEntity);
        }

        // Sort the rules by order value, then set the list to resource
        mockRuleEntities.sort(Comparator.comparingInt(MockRuleEntity::getOrder));
        mockResourceEntity.getRules().addAll(mockRuleEntities);
        return mockResourceEntity;
    }

    private List<TagEntity> getTagEntities(List<String> tags) {
        List<TagEntity> tagEntities = new ArrayList<>();
        for (String tag : tags) {
            tagEntities.add(
                    tagRepository.findByValue(tag).orElseGet(() -> tagRepository.save(TagEntity.builder()
                            .id(Utility.generateUUID())
                            .value(tag)
                            .build()))
            );
        }
        return tagEntities;
    }
}
