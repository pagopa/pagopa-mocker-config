package it.gov.pagopa.mockconfig.util.validation;

import it.gov.pagopa.mockconfig.entity.ArchetypeEntity;
import it.gov.pagopa.mockconfig.entity.ArchetypeParameterEntity;
import it.gov.pagopa.mockconfig.entity.ArchetypeResponseEntity;
import it.gov.pagopa.mockconfig.exception.AppError;
import it.gov.pagopa.mockconfig.exception.AppException;
import it.gov.pagopa.mockconfig.model.archetype.MockRuleFromArchetype;
import it.gov.pagopa.mockconfig.model.archetype.StaticParameterValue;
import it.gov.pagopa.mockconfig.model.enumeration.ArchetypeParameterType;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArchetypeValidation {

    public static void checkURLParameters(List<ArchetypeParameterEntity> parametersToBeSet, List<StaticParameterValue> passedParameters) {
        Set<String> filteredParametersToBeSet = parametersToBeSet.stream()
                .filter(archetypeParameterEntity -> ArchetypeParameterType.PATH.equals(archetypeParameterEntity.getType()))
                .map(archetypeParameterEntity -> archetypeParameterEntity.getId().getName())
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
}
