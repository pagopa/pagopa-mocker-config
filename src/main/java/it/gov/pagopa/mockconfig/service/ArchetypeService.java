package it.gov.pagopa.mockconfig.service;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.exception.AppError;
import it.gov.pagopa.mockconfig.exception.AppException;
import it.gov.pagopa.mockconfig.mapper.ConvertMockResourceFromArchetypeToMockResource;
import it.gov.pagopa.mockconfig.model.archetype.*;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import it.gov.pagopa.mockconfig.model.mockresource.MockResourceList;
import it.gov.pagopa.mockconfig.repository.ArchetypeRepository;
import it.gov.pagopa.mockconfig.repository.MockResourceRepository;
import it.gov.pagopa.mockconfig.util.OpenAPIExtractor;
import it.gov.pagopa.mockconfig.util.Utility;
import it.gov.pagopa.mockconfig.util.validation.RequestSemanticValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ArchetypeService {

    @Autowired private MockTagService mockTagService;

    @Autowired private MockResourceRepository mockResourceRepository;

    @Autowired private ArchetypeRepository archetypeRepository;

    @Autowired private ModelMapper modelMapper;

    public ArchetypeHandlingResult importArchetypesFromOpenAPI(MultipartFile file, String subsystem) {
        ArchetypeHandlingResult archetypeHandlingResult;
        try {
            // Parsing file content (json or yaml) into OpenAPI Object
            SwaggerParseResult parsedOpenAPI = new OpenAPIParser().readContents(new String(file.getBytes()), List.of(), null);

            // Generate archetype from the extracted OpenAPI content
            List<ArchetypeEntity> archetypeEntities = OpenAPIExtractor.extractArchetype(parsedOpenAPI.getOpenAPI(), subsystem);
            archetypeEntities.forEach(entity -> entity.setTags(mockTagService.validateResourceTags(entity.getTags())));

            // Filter only the archetypes that are not already present in DB
            List<ArchetypeEntity> newArchetypeEntites = archetypeEntities.stream()
                    .filter(entity -> archetypeRepository.findBySubsystemUrlAndResourceUrlAndHttpMethod(entity.getSubsystemUrl(), entity.getResourceUrl(), entity.getHttpMethod()).isEmpty())
                    .collect(Collectors.toList());

            // Save the generated resource
            newArchetypeEntites = archetypeRepository.saveAll(newArchetypeEntites);
            archetypeHandlingResult = ArchetypeHandlingResult.builder()
                    .generatedArchetypes(newArchetypeEntites.size())
                    .subsystemURL(subsystem)
                    .build();

        } catch (IOException e) {
            log.error("An error occurred while trying to parse the passed OpenAPI file. ", e);
            throw new AppException(AppError.OPENAPI_IMPORT_INVALID_FILE_CONTENT);
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to persist a list of archetypes. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return archetypeHandlingResult;
    }

    public ArchetypeList getArchetypes(Pageable pageable) {
        List<Archetype> archetypes;
        Page<ArchetypeEntity> archetypePaginatedEntities;
        try {
            archetypePaginatedEntities = archetypeRepository.findAll(pageable);
            archetypes = archetypePaginatedEntities.stream()
                    .map(archetype -> modelMapper.map(archetype, Archetype.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to retrieve a list of archetypes. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return ArchetypeList.builder()
                .archetypes(archetypes)
                .pageInfo(Utility.buildPageInfo(archetypePaginatedEntities))
                .build();
    }

    public Archetype getArchetype(String archetypeId) {
        ArchetypeEntity archetypeEntity;
        try {
            archetypeEntity = archetypeRepository.findById(archetypeId)
                    .orElseThrow(() -> new AppException(AppError.ARCHETYPE_NOT_FOUND, archetypeId));
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to retrieve the detail of an archetype. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return modelMapper.map(archetypeEntity, Archetype.class);
    }

    public Archetype createArchetype(Archetype archetype) {
        Archetype response;
        try {

            // check request semantic validity
            RequestSemanticValidator.validate(archetype);

            // Search if the archetype already exists
            String subsystem = archetype.getSubsystem();
            String resourceUrl = archetype.getResourceURL();
            HttpMethod httpMethod = archetype.getHttpMethod();
            archetypeRepository.findBySubsystemUrlAndResourceUrlAndHttpMethod(subsystem, resourceUrl, httpMethod)
                    .ifPresent(res -> {throw new AppException(AppError.ARCHETYPE_CONFLICT, httpMethod ,subsystem + resourceUrl); });

            // Persisting the archetype
            response = persistArchetype(archetype);

        } catch (DataAccessException e) {
            log.error("An error occurred while trying to create an archetype. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return response;
    }


    public ArchetypeHandlingResult deleteByCriteria(String subsystem) {
        ArchetypeHandlingResult archetypeHandlingResult;
        try {
            // TODO temporary, deletion only by subsystem
            List<ArchetypeEntity> archetypeEntities = archetypeRepository.findBySubsystemUrl(subsystem);
            archetypeRepository.deleteAll(archetypeEntities);
            archetypeHandlingResult = ArchetypeHandlingResult.builder()
                    .generatedArchetypes(archetypeEntities.size())
                    .subsystemURL(subsystem)
                    .build();
        } catch (DataAccessException e) {
            log.error("An error occurred while trying to delete archetypes by criteria. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return archetypeHandlingResult;
    }

    public MockResource createMockResourceFromArchetype(String archetypeId, MockResourceFromArchetype mockResourceFromArchetype) {
        MockResource response;
        try {

            // Search if the archetype exists
            ArchetypeEntity archetypeEntity = archetypeRepository.findById(archetypeId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "", ""));

            // check request semantic validity
            RequestSemanticValidator.validate(mockResourceFromArchetype, archetypeEntity);

            // generating resource URL including the parameters passed as input
            String resourceUrl = archetypeEntity.getResourceUrl();
            for (StaticParameterValue urlParameter : mockResourceFromArchetype.getUrlParameters()) {
                resourceUrl = resourceUrl.replaceAll("\\{" + urlParameter.getName() + "\\}", urlParameter.getValue());
            }
            log.info(String.format("Generating mock resource with resource URL [%s] from archetype with id [%s]", resourceUrl, archetypeId));

            // Search if the resource already exists
            Long id = Utility.generateResourceId(archetypeEntity.getHttpMethod(), archetypeEntity.getSubsystemUrl(), resourceUrl);
            mockResourceRepository.findById(id).ifPresent(res -> {throw new AppException(AppError.MOCK_RESOURCE_CONFLICT, id); });

            // Map entity from input model, setting id and tags and completing the entities' tree
            MockResourceEntity mockResourceEntity = ConvertMockResourceFromArchetypeToMockResource.convert(mockResourceFromArchetype, archetypeEntity, resourceUrl);
            mockResourceEntity.setTags(mockTagService.validateResourceTags(mockResourceEntity.getTags()));
            mockResourceEntity.getRules().forEach(rule -> rule.setTags(mockTagService.validateRuleTags(rule.getTags())));

            // Persisting the mock resource
            mockResourceEntity = mockResourceRepository.save(mockResourceEntity);
            response = modelMapper.map(mockResourceEntity, MockResource.class);

        } catch (DataAccessException e) {
            log.error("An error occurred while trying to create a mock resource from archetype. ", e);
            throw new AppException(AppError.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private Archetype persistArchetype(Archetype archetype) {
        // Map entity from input model, setting id and tags and completing the entities' tree
        ArchetypeEntity archetypeEntity = modelMapper.map(archetype, ArchetypeEntity.class);
        archetypeEntity.setTags(mockTagService.validateResourceTags(archetypeEntity.getTags()));

        // Save the converted resource
        archetypeEntity = archetypeRepository.save(archetypeEntity);
        return modelMapper.map(archetypeEntity, Archetype.class);
    }

}
