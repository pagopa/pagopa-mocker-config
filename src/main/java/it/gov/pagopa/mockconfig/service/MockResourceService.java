package it.gov.pagopa.mockconfig.service;

import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.exception.AppError;
import it.gov.pagopa.mockconfig.exception.AppException;
import it.gov.pagopa.mockconfig.model.mockresource.*;
import it.gov.pagopa.mockconfig.repository.MockResourceRepository;
import it.gov.pagopa.mockconfig.util.validation.RequestSemanticValidator;
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

    @Autowired private MockTagService mockTagService;

    @Autowired private MockResourceRepository mockResourceRepository;

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

    public MockResource getMockResource(String id) {
        MockResourceEntity mockResourceEntity;
        try {
            mockResourceEntity = mockResourceRepository.findById(id)
                    .orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, id));
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
            RequestSemanticValidator.validate(mockResource);

            // Search if the resource already exists
            String id = Utility.generateResourceId(mockResource);
            mockResourceRepository.findById(id).ifPresent(res -> {throw new AppException(AppError.MOCK_RESOURCE_CONFLICT, id); });

            // Persisting the mock resource
            response = persistMockResource(mockResource);

        } catch (DataAccessException e) {
            log.error("An error occurred while trying to create a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MockResource updateMockResource(String id, MockResource mockResource) {
        MockResource response;
        try {

            // check request semantic validity
            RequestSemanticValidator.validate(mockResource);

            // Check if passed resource identifier is equals to the one generable by body content
            String generatedId = Utility.generateResourceId(mockResource);
            if (!generatedId.equals(id)) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_ID, id, generatedId);
            }

            // Search if the resource exists
            MockResourceEntity mockResourceEntity = mockResourceRepository.findById(id).orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, id));
            if (!isResourceURLNotChanged(mockResource, mockResourceEntity)) {
                throw new AppException(AppError.MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_URL, id, mockResourceEntity.getId());
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

    public void deleteMockResource(String id) {
        try {
            MockResourceEntity mockResourceEntity = mockResourceRepository.findById(id)
                    .orElseThrow(() -> new AppException(AppError.MOCK_RESOURCE_NOT_FOUND, id));
            mockResourceRepository.delete(mockResourceEntity);
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to delete a mock resource. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
    }

    private MockResource persistMockResource(MockResource mockResource) {
        // Map entity from input model, setting id and tags and completing the entities' tree
        MockResourceEntity mockResourceEntity = modelMapper.map(mockResource, MockResourceEntity.class);
        mockResourceEntity.setTags(mockTagService.validateResourceTags(mockResourceEntity.getTags()));
        mockResourceEntity.getRules().forEach(rule -> rule.setTags(mockTagService.validateRuleTags(rule.getTags())));

        // Save the converted resource
        mockResourceEntity = mockResourceRepository.saveAndFlush(mockResourceEntity);
        return modelMapper.map(mockResourceEntity, MockResource.class);
    }

    private boolean isResourceURLNotChanged(MockResource mockResource, MockResourceEntity mockResourceEntity) {
        String mockResourceEntityResourceUrl =  Utility.deNull(mockResourceEntity.getResourceUrl());
        String mockResourceResourceUrl =  Utility.deNull(mockResource.getResourceURL());
        String mockResourceEntitySubsystemUrl =  Utility.deNull(mockResourceEntity.getSubsystemUrl());
        String mockResourceSubsystemUrl =  Utility.deNull(mockResource.getSubsystem());
        return mockResourceEntityResourceUrl.equals(mockResourceResourceUrl) && mockResourceEntitySubsystemUrl.equals(mockResourceSubsystemUrl);
    }

}
