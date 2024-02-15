package it.gov.pagopa.mocker.config.util.validation;

import it.gov.pagopa.mocker.config.entity.ArchetypeEntity;
import it.gov.pagopa.mocker.config.entity.ArchetypeParameterEntity;
import it.gov.pagopa.mocker.config.entity.ArchetypeResponseEntity;
import it.gov.pagopa.mocker.config.exception.AppError;
import it.gov.pagopa.mocker.config.exception.AppException;
import it.gov.pagopa.mocker.config.model.archetype.Archetype;
import it.gov.pagopa.mocker.config.model.archetype.MockRuleFromArchetype;
import it.gov.pagopa.mocker.config.model.archetype.StaticParameterValue;
import it.gov.pagopa.mocker.config.model.enumeration.ArchetypeParameterType;
import it.gov.pagopa.mocker.config.util.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class ArchetypeValidation {

    public static void checkURLParameters(Archetype archetype) {
        List<String> parameters = Utility.extractURLParameters(archetype.getResourceURL());
        Set<String> urlParametersExplicitlySet = Set.copyOf(archetype.getUrlParameters());
        for (String urlParameter : parameters) {
            if (!urlParametersExplicitlySet.contains(urlParameter)) {
                throw new AppException(AppError.ARCHETYPE_BAD_REQUEST_MISSING_URL_PARAMETER, parameters);
            }
        }
    }


    public static void checkURLParameters(List<ArchetypeParameterEntity> parametersToBeSet, List<StaticParameterValue> passedParameters) {
        Set<String> filteredParametersToBeSet = parametersToBeSet.stream()
                .filter(archetypeParameterEntity -> ArchetypeParameterType.PATH.equals(archetypeParameterEntity.getType()))
                .map(ArchetypeParameterEntity::getName)
                .collect(Collectors.toSet());
        for (StaticParameterValue parameterInRequest : passedParameters) {
            if (!filteredParametersToBeSet.contains(parameterInRequest.getName())) {
                throw new AppException(AppError.MOCK_RESOURCE_GENERATION_FROM_ARCHETYPE_MISSING_URL_PARAMETER, filteredParametersToBeSet.toString());
            }
        }
    }

    public static void checkResponseAssociationToValidHTTPStatus(ArchetypeEntity archetypeEntity, MockRuleFromArchetype mockRuleFromArchetype) {
        int httpStatus = mockRuleFromArchetype.getResponse().getStatus();
        boolean found = false;

        // iterating while the element is found or while there are other elements
        Iterator<ArchetypeResponseEntity> it = archetypeEntity.getResponses().iterator();
        while (it.hasNext() && !found) {
            ArchetypeResponseEntity storedArchetypeResponseEntity = it.next();
            found = storedArchetypeResponseEntity.getStatus() == httpStatus;
        }
        if (!found) {
            throw new AppException(AppError.MOCK_RESOURCE_GENERATION_FROM_ARCHETYPE_INVALID_HTTPSTATUS, httpStatus);
        }
    }

    public static void checkUniqueHttpCodes(List<Integer> httpCodes) {
        Set<Integer> uniqueHttpCodes = new HashSet<>(httpCodes);
        if (uniqueHttpCodes.size() != httpCodes.size()) {
            throw new AppException(AppError.ARCHETYPE_BAD_REQUEST_MULTIPLE_RESPONSE_WITH_SAME_HTTPCODE);
        }
    }

    public static void checkURLCongruency(Archetype archetype, ArchetypeEntity archetypeEntity) {
        boolean isWrong = !archetypeEntity.getSubsystemUrl().equals(archetype.getSubsystem()) ||
                !archetypeEntity.getResourceUrl().equals(archetype.getResourceURL()) ||
                !archetypeEntity.getHttpMethod().equals(archetype.getHttpMethod());
        if (isWrong) {
            throw new AppException(AppError.ARCHETYPE_BAD_REQUEST_INVALID_RESOURCE_URL);
        }
    }
}
