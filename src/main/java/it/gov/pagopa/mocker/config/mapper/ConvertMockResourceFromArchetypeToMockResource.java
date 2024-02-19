package it.gov.pagopa.mocker.config.mapper;

import it.gov.pagopa.mocker.config.entity.*;
import it.gov.pagopa.mocker.config.model.archetype.MockResourceFromArchetype;
import it.gov.pagopa.mocker.config.model.archetype.MockResponseFromArchetype;
import it.gov.pagopa.mocker.config.model.archetype.MockRuleFromArchetype;
import it.gov.pagopa.mocker.config.model.mockresource.MockCondition;
import it.gov.pagopa.mocker.config.util.Utility;

import java.util.*;
import java.util.stream.Collectors;

public class ConvertMockResourceFromArchetypeToMockResource {

    private ConvertMockResourceFromArchetypeToMockResource() {
    }

    public static MockResourceEntity convert(MockResourceFromArchetype mockResourceFromArchetype, ArchetypeEntity archetypeEntity, String resourceUrl) {

        // generating the mock resource
        MockResourceEntity mockResourceEntity = MockResourceEntity.builder()
                .id(Utility.generateResourceId(archetypeEntity.getHttpMethod(), archetypeEntity.getSubsystemUrl(), resourceUrl, List.of()))
                .name(archetypeEntity.getName())
                .subsystemUrl(archetypeEntity.getSubsystemUrl())
                .resourceUrl(resourceUrl)
                .specialHeaders(List.of())
                .httpMethod(archetypeEntity.getHttpMethod())
                .isActive(mockResourceFromArchetype.isActive())
                .rules(new LinkedList<>())
                .archetypeId(archetypeEntity.getId())
                .build();

        // iterating for each rule defined in the passed input, searching if there is a correlation to the one saved in the archetype
        for (MockRuleFromArchetype mockRuleFromArchetype : mockResourceFromArchetype.getRules()) {

            // retrieving the response related to the saved archetype that correspond to the HTTP status included in the rule in input
            ArchetypeResponseEntity archetypeResponseEntity = extractArchetypeResponseEntityFromPassedHTTPStatus(archetypeEntity, mockRuleFromArchetype);

            // retrieving the formatted body to be assigned to the response
            String body = reformatResponseBody(archetypeResponseEntity, mockRuleFromArchetype);

            // generating the injectable parameter list, taking the reformatted body searching if some parameter respect the format '${...}' on its value
            List<String> injectableParameters = Utility.extractInjectableParameters(body);

            // retrieving the response (passed as input) related to the rule to be generated following the archetype
            MockResponseFromArchetype mockResponseFromArchetype = mockRuleFromArchetype.getResponse();

            // generating the mock response related to the rule
            MockResponseEntity mockResponseEntity = MockResponseEntity.builder()
                    .body(Base64.getEncoder().encodeToString(body.getBytes()))
                    .status(mockResponseFromArchetype.getStatus())
                    .headers(new LinkedList<>())
                    .parameters(new HashSet<>(injectableParameters))
                    .build();

            // include the headers, either the one passed as input and the ones defined in the archetype
            setHeaderToMockResponse(mockResponseEntity, archetypeResponseEntity, mockResponseFromArchetype);

            // generating the mock rule
            MockRuleEntity mockRuleEntity = MockRuleEntity.builder()
                    .id(Utility.generateUUID())
                    .name(mockRuleFromArchetype.getName())
                    .order(mockRuleFromArchetype.getOrder())
                    .isActive(mockResourceFromArchetype.isActive())
                    .response(mockResponseEntity)
                    .build();
            mockRuleEntity.setConditions(extractMockConditions(mockRuleFromArchetype, mockRuleEntity));
            mockResourceEntity.getRules().add(mockRuleEntity);
        }
        return mockResourceEntity;
    }

    private static ArchetypeResponseEntity extractArchetypeResponseEntityFromPassedHTTPStatus(ArchetypeEntity archetypeEntity, MockRuleFromArchetype mockRuleFromArchetype) {
        ArchetypeResponseEntity archetypeResponseEntity = null;
        int httpStatus = mockRuleFromArchetype.getResponse().getStatus();
        boolean found = false;

        // iterating while the element is found or while there are other elements. This could not be never null because the
        // request validation confirmed that the response is associated to an existent HTTP status.
        Iterator<ArchetypeResponseEntity> it = archetypeEntity.getResponses().iterator();
        while (it.hasNext() && !found) {
            ArchetypeResponseEntity storedArchetypeResponseEntity = it.next();
            found = storedArchetypeResponseEntity.getStatus() == httpStatus;
            if (found) {
                archetypeResponseEntity = storedArchetypeResponseEntity;
            }
        }
        return archetypeResponseEntity;
    }

    private static String reformatResponseBody(ArchetypeResponseEntity archetypeResponseEntity, MockRuleFromArchetype mockRuleFromArchetype) {
        String body = null;
        /* TODO
        ArchetypeSchemaEntity schema = archetypeResponseEntity.getSchema();
        if (schema != null) {
            body = new String(Base64.getDecoder().decode(schema.getContent().getBytes()));
            for (StaticParameterValue staticValue : mockRuleFromArchetype.getResponse().getStaticValues()) {
                body = body.replaceAll("\\$\\{" + staticValue.getName() + "\\}", staticValue.getValue());
            }
        }*/
        return body;
    }

    private static void setHeaderToMockResponse(MockResponseEntity mockResponseEntity, ArchetypeResponseEntity archetypeResponseEntity, MockResponseFromArchetype mockResponseFromArchetype) {
        // adding the headers to the mock response related to the rule
        mockResponseEntity.getHeaders().addAll(mockResponseFromArchetype.getHeaders()
                .stream()
                .map(header -> ResponseHeaderEntity.builder()
                        .header(header.getName())
                        .value(header.getValue())
                        .build()
                )
                .collect(Collectors.toList())
        );

        // add an header defined in the archetype if the same header is not passed from an header in the request
        List<ArchetypeResponseHeaderEntity> headersSetInArchetypeToBeAdded = new LinkedList<>();
        for (ArchetypeResponseHeaderEntity headerTemplateSetInArchetype : archetypeResponseEntity.getHeaders()) {
            boolean exist = false;
            Iterator<ResponseHeaderEntity> it = mockResponseEntity.getHeaders().iterator();

            // checking if the header already set and generated by the input body already exists. If not exists, i.e. it was
            // not passed in input, it will be added in order to be saved as template placeholder
            while (!exist && it.hasNext()) {
                ResponseHeaderEntity headerAlreadySetFromRequest = it.next();
                exist = true; /* TODO headerAlreadySetFromRequest.getHeader().equals(headerTemplateSetInArchetype.getHeader());*/
            }
            if (!exist) {
                headersSetInArchetypeToBeAdded.add(headerTemplateSetInArchetype);
            }
        }

        // finally, add the header defined in the archetype not overridden in input
        /* TODO
        mockResponseEntity.getHeaders().addAll(headersSetInArchetypeToBeAdded.stream()
                .map(header -> ResponseHeaderEntity.builder()
                        .header(header.getId().getHeader())
                        .value(header.getValue())
                        .build()
                )
                .collect(Collectors.toList())
        );*/
    }

    private static List<MockConditionEntity> extractMockConditions(MockRuleFromArchetype mockRuleFromArchetype, MockRuleEntity mockRuleEntity) {
        List<MockConditionEntity> conditionEntities = new LinkedList<>();
        for (MockCondition mockCondition : mockRuleFromArchetype.getConditions()) {
            mockCondition.setIdIfNull(Utility.generateUUID());
            conditionEntities.add(
                    MockConditionEntity.builder()
                            .id(mockCondition.getId())
                            .order(mockCondition.getOrder())
                            .fieldPosition(mockCondition.getFieldPosition())
                            .analyzedContentType(mockCondition.getAnalyzedContentType())
                            .fieldName(mockCondition.getFieldName())
                            .conditionType(mockCondition.getConditionType())
                            .conditionValue(mockCondition.getConditionValue())
                            .build()
            );
        }
        conditionEntities.sort(Comparator.comparingInt(MockConditionEntity::getOrder));
        return conditionEntities;
    }
}
