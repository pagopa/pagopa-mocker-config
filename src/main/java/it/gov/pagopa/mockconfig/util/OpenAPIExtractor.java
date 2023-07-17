package it.gov.pagopa.mockconfig.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import it.gov.pagopa.mockconfig.entity.*;
import it.gov.pagopa.mockconfig.entity.embeddable.ArchetypeParameterKey;
import it.gov.pagopa.mockconfig.entity.embeddable.ArchetypeResponseHeaderKey;
import it.gov.pagopa.mockconfig.model.enumeration.ArchetypeParameterType;
import it.gov.pagopa.mockconfig.model.enumeration.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
public class OpenAPIExtractor {

    private OpenAPIExtractor() {}

    public static List<ArchetypeEntity> extractArchetype(OpenAPI openAPI, String subsystem) {
        List<ArchetypeEntity> archetypeEntities = new LinkedList<>();

        Map<String, Map<String, Schema>> rawClasses = getClassesStructure(openAPI);

        // start with the analysis of the PathItem, i.e. the APIs categorized by path string
        Paths paths = openAPI.getPaths();
        for (Map.Entry<String, PathItem> entry : paths.entrySet()) {

            /* extracting a set of APIs from the same path. From this moment, it is possible to have
               GET, PUT, POST, DELETE and other APIs that have the same path.
             */
            Map<String, Operation> operations = getOperations(entry.getValue());
            for (Map.Entry<String, Operation> operationEntry : operations.entrySet()) {

                // extracting information about the operation under analysis
                Operation operation = operationEntry.getValue();
                HttpMethod operationHTTPMethod = HttpMethod.valueOf(operationEntry.getKey());

                // generating the archetype identifier as a UUID string
                String archetypeId = Utility.generateUUID();

                // defining the archetype entity structure
                List<ArchetypeParameterEntity> parameterEntities = new LinkedList<>();
                List<ArchetypeResponseEntity> responseEntities = new LinkedList<>();
                ArchetypeEntity archetypeEntity = ArchetypeEntity.builder()
                        .id(archetypeId)
                        .name(operation.getSummary())
                        .subsystemUrl(subsystem)
                        .resourceUrl(entry.getKey())
                        .httpMethod(operationHTTPMethod)
                        .parameters(parameterEntities)
                        .responses(responseEntities)
                        .tags(extractTagsFromOperation(operation))
                        .build();

                // defining the archetype tags
                parameterEntities.addAll(extractParametersFromOperation(operation, archetypeEntity));

                // defining the response for the archetype, one for each HTTP Status code expecred for the resource
                responseEntities.addAll(extractResponsesFromOperation(operation, rawClasses, archetypeEntity));

                // add the generated archetype on the list to be returned as result
                archetypeEntities.add(archetypeEntity);
            }
        }

        return archetypeEntities;
    }

    private static Map<String, Operation> getOperations(PathItem item) {
        Operation operation;
        Map<String, Operation> entries = new HashMap<>();
        if ((operation = item.getGet()) != null) {
            entries.put("GET", operation);
        }
        if ((operation = item.getPost()) != null) {
            entries.put("POST", operation);
        }
        if ((operation = item.getPut()) != null) {
            entries.put("PUT", operation);
        }
        if ((operation = item.getDelete()) != null) {
            entries.put("DELETE", operation);
        }
        if ((operation = item.getPatch()) != null) {
            entries.put("PATCH", operation);
        }
        if ((operation = item.getOptions()) != null) {
            entries.put("OPTIONS", operation);
        }
        if ((operation = item.getTrace()) != null) {
            entries.put("TRACE", operation);
        }
        return entries;
    }

    private static Map<String, Map<String, Schema>> getClassesStructure(OpenAPI openAPI) {
        Map<String, Map<String, Schema>> rawClasses = new HashMap<>();

        // get the schemas from OpenAPI's components and, if not null, extract the parameters
        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
        if (schemas != null) {

            // analyze all the schemas in order to extract the parameters and add to raw classes map
            for (Map.Entry<String, Schema> schemaEntry : schemas.entrySet()) {

                // extracting the raw parameters that will be included in the map and that will be used for the request body
                Schema schema = schemaEntry.getValue();
                Map<String, Schema> parameters = new HashMap<>();
                for (Object entryObj : schema.getProperties().entrySet()) {
                    Map.Entry<String, Schema> entry = ((Map.Entry<String, Schema>) entryObj);
                    parameters.put(entry.getKey(), entry.getValue());
                }

                // insert the parameter map in the raw classes
                rawClasses.put(schemaEntry.getKey(), parameters);
            }
        }
        return rawClasses;
    }

    private static List<ResourceTagEntity> extractTagsFromOperation(Operation operation) {
        List<ResourceTagEntity> resourceTagEntities = new LinkedList<>();

        // add tag created by operation identifier
        resourceTagEntities.add(ResourceTagEntity.builder()
                .id(Utility.generateUUID())
                .value(operation.getOperationId())
                .build()
        );

        // add tags created by operation tags
        for (String tag : operation.getTags()) {
            resourceTagEntities.add(ResourceTagEntity.builder()
                    .id(Utility.generateUUID())
                    .value(tag)
                    .build()
            );
        }
        return resourceTagEntities;
    }

    private static List<ArchetypeParameterEntity> extractParametersFromOperation(Operation operation, ArchetypeEntity archetypeEntity) {
        List<ArchetypeParameterEntity> parameterEntities = new LinkedList<>();

        // if the operation contains parameters, add them as a new entity
        if (operation.getParameters() != null) {
            for (Parameter parameter : operation.getParameters()) {

                // add a new parameter starting from operation's parameter
                parameterEntities.add(ArchetypeParameterEntity.builder()
                        .id(ArchetypeParameterKey.builder()
                                .archetypeId(archetypeEntity.getId())
                                .name(parameter.getName())
                                .build()
                        )
                        .type(ArchetypeParameterType.valueOf(parameter.getIn().toUpperCase()))
                        .archetype(archetypeEntity)
                        .build()
                );
            }
        }
        return parameterEntities;
    }

    private static List<ArchetypeResponseEntity> extractResponsesFromOperation(Operation operation, Map<String, Map<String, Schema>> rawClasses, ArchetypeEntity archetypeEntity) {
        List<ArchetypeResponseEntity> responseEntities = new LinkedList<>();

        // defining the response for the archetype, one for each HTTP Status code expected for the resource
        for (Map.Entry<String, ApiResponse> responseEntry : operation.getResponses().entrySet()) {

            // extracting the response
            List<ArchetypeResponseHeaderEntity> responseHeaderEntities = new LinkedList<>();
            ApiResponse response = responseEntry.getValue();
            Pair<String, String> content = extractBodyContentFromReferencedClass(response, rawClasses);
            ArchetypeResponseEntity archetypeResponseEntity = ArchetypeResponseEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .body(content.getRight())
                    .status(Integer.parseInt(responseEntry.getKey()))
                    .headers(responseHeaderEntities)
                    .archetypeId(archetypeEntity.getId())
                    .archetype(archetypeEntity)
                    .build();

            // extracting the headers
            responseHeaderEntities.addAll(extractHeadersFromOperation(response, archetypeResponseEntity));
            responseHeaderEntities.add(ArchetypeResponseHeaderEntity.builder()
                    .id(ArchetypeResponseHeaderKey.builder()
                            .archetypeResponseId(archetypeResponseEntity.getId())
                            .header("Content-Type") // add content-type from extracted content
                            .build()
                    )
                    .response(archetypeResponseEntity)
                    .value(content.getLeft())
                    .build()
            );

            // add extracted response
            responseEntities.add(archetypeResponseEntity);
        }

        return responseEntities;
    }

    private static List<ArchetypeResponseHeaderEntity> extractHeadersFromOperation(ApiResponse apiResponse, ArchetypeResponseEntity archetypeResponseEntity) {
        List<ArchetypeResponseHeaderEntity> headerEntities = new LinkedList<>();

        // if the operation contains headers, add them as a new entity
        if (apiResponse.getHeaders() != null) {
            for (Map.Entry<String, Header> headerEntry : apiResponse.getHeaders().entrySet()) {

                // add a new header starting from operation's header
                headerEntities.add(ArchetypeResponseHeaderEntity.builder()
                        .id(ArchetypeResponseHeaderKey.builder()
                                .archetypeResponseId(archetypeResponseEntity.getId())
                                .header(headerEntry.getKey())
                                .build()
                        )
                        .value("-")
                        .response(archetypeResponseEntity)
                        .build()
                );
            }
        }
        return headerEntities;
    }

    private static Pair<String, String> extractBodyContentFromReferencedClass(ApiResponse apiResponse, Map<String, Map<String, Schema>> rawClasses) {
        String bodyContent = apiResponse.get$ref();
        String contentType = "";

        // if the operation contains a response body, add them as a new entity and associate it to an HTTP status code
        if (apiResponse.getContent() != null) {

            // note: for dynamism, it will be handled with iterator but it will contains only a single media-type per content
            for (Map.Entry<String, MediaType> contentMediaTypeEntry : apiResponse.getContent().entrySet()) {

                contentType = contentMediaTypeEntry.getKey();
                switch (contentMediaTypeEntry.getKey()) {

                    // generate the JSON response body
                    case "application/json":
                        bodyContent = extractJSONFromReferencedClass(contentMediaTypeEntry.getValue(), rawClasses);
                        break;

                    // generate the JSON response body
                    case "application/xml":
                    case "text/xml":
                        bodyContent = extractXMLFromReferencedClass(contentMediaTypeEntry.getValue(), rawClasses);
                        break;

                    // generate raw data for plain text and binary data
                    case "text/plain":
                    case "application/octet-stream":
                        bodyContent = "${content}";
                        break;

                    // generate the response body from another content-type (currently not supported)
                    default:
                        log.warn(String.format("Content media type not supported [%s]", contentMediaTypeEntry.getKey()));
                        break;
                }
            }
        }

        return new ImmutablePair<>(contentType, bodyContent);
    }

    private static String extractJSONFromReferencedClass(MediaType contentMediaType, Map<String, Map<String, Schema>> rawClasses) {
        String bodyContent = null;
        Schema schema = contentMediaType.getSchema();
        if (schema != null) {

            // get class reference: if it exists, get the class and convert it to a valid JSON content
            String className = schema.get$ref();
            if (className != null) {
                String plainContent = convertRawClassToJSON(rawClasses, className.substring(className.lastIndexOf("/") + 1));
                bodyContent = Base64.getEncoder().encodeToString(plainContent.getBytes());
            }
        }
        return bodyContent;
    }

    private static String extractXMLFromReferencedClass(MediaType contentMediaType, Map<String, Map<String, Schema>> rawClasses) {
        String bodyContent = null;
        Schema schema = contentMediaType.getSchema();
        if (schema != null) {

            // get class reference: if it exists, get the class and convert it to a valid JSON content
            String className = schema.get$ref();
            if (className != null) {
                String plainContent = convertRawClassToXML(rawClasses, className.substring(className.lastIndexOf("/") + 1));
                bodyContent = Base64.getEncoder().encodeToString(plainContent.getBytes());
            }
        }
        return bodyContent;
    }

    private static String convertRawClassToJSON(Map<String, Map<String, Schema>> rawClasses, String className) {

        StringBuilder stringBuilder = new StringBuilder();

        // get raw class from passed class name: if exists, execute the conversion
        Map<String, Schema> rawClass = rawClasses.get(className);
        if (rawClass != null) {

            // JSON starting charachter
            stringBuilder.append("{");

            List<Map.Entry<String, Schema>> orderedParameters = new ArrayList<>(rawClass.entrySet());
            orderedParameters.sort(Map.Entry.comparingByKey());
            Iterator<Map.Entry<String, Schema>> it = orderedParameters.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Schema> entry = it.next();

                // extracted needed parameters
                String parameterName = entry.getKey();
                Schema classSchema = entry.getValue();

                // extract and insert the parameter name (left section of the parameter)
                stringBuilder.append("\"").append(parameterName).append("\": ");

                // construct the correct object from the nested schema type
                String type = classSchema.getType();

                if (type != null) {
                    switch (type) {

                        // if type of the nested schema is an array, extract the JSON object recursively and encapsulate it in squared brackets
                        case "array":
                            stringBuilder.append("[").append(convertRawClassToJSON(rawClasses, getNestedClassNameFromSchema(classSchema))).append("]");
                            break;

                        // if type of the nested schema is a string, set the parameter name as injectable parameter included in " characters
                        case "string":
                            stringBuilder.append("\"").append("${").append(parameterName).append("}\"");
                            break;

                        // if type of the nested schema is a primitive type (integer, boolean, etc), set the parameter name as injectable parameter but not included in " characters
                        default:
                            stringBuilder.append("${").append(parameterName).append("}");
                            break;
                    }
                }

                // if the nested schema is a complex type, extract the JSON object recursively
                else if (classSchema.getItems() != null) {
                    stringBuilder.append(convertRawClassToJSON(rawClasses, getNestedClassNameFromSchema(classSchema)));
                }

                // if the nested schema is a complex type referenced directly to object, extract the JSON object recursively
                else if (classSchema.get$ref() != null) {
                    stringBuilder.append(convertRawClassToJSON(rawClasses, getClassNameFromReference(classSchema.get$ref())));
                }

                // add comma if not last parameter
                if (it.hasNext()) {
                    stringBuilder.append(", ");
                }
            }

            // JSON ending charachter
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    private static String convertRawClassToXML(Map<String, Map<String, Schema>> rawClasses, String className) {

        StringBuilder stringBuilder = new StringBuilder();

        // get raw class from passed class name: if exists, execute the conversion
        Map<String, Schema> rawClass = rawClasses.get(className);
        if (rawClass != null) {

            // XML starting character
            stringBuilder.append("<").append(className).append(">");

            List<Map.Entry<String, Schema>> orderedParameters = new ArrayList<>(rawClass.entrySet());
            orderedParameters.sort(Map.Entry.comparingByKey());
            Iterator<Map.Entry<String, Schema>> it = orderedParameters.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Schema> entry = it.next();

                // extracted needed parameters
                String parameterName = entry.getKey();
                Schema classSchema = entry.getValue();

                // extract and insert the parameter name (left section of the parameter)
                stringBuilder.append("<").append(parameterName).append(">");

                // construct the correct object from the nested schema type
                String type = classSchema.getType();

                if (type != null) {
                    switch (type) {

                        // if type of the nested schema is an array, extract the JSON object recursively and encapsulate it in squared brackets
                        case "array":
                            stringBuilder.append("<").append(parameterName).append(">")
                                    .append(convertRawClassToXML(rawClasses, getNestedClassNameFromSchema(classSchema)))
                                    .append("</").append(parameterName).append(">");
                            break;

                        // if type of the nested schema is a primitive type (integer, boolean, etc), set the parameter name as injectable parameter but not included in " characters
                        default:
                            stringBuilder.append("${").append(parameterName).append("}");
                            break;
                    }
                }

                // if the nested schema is a complex type, extract the JSON object recursively
                else if (classSchema.getItems() != null) {
                    stringBuilder.append(convertRawClassToXML(rawClasses, getNestedClassNameFromSchema(classSchema)));
                }

                // if the nested schema is a complex type referenced directly to object, extract the JSON object recursively
                else if (classSchema.get$ref() != null) {
                    stringBuilder.append(convertRawClassToXML(rawClasses, getClassNameFromReference(classSchema.get$ref())));
                }

                // add comma if not last parameter
                stringBuilder.append("</").append(parameterName).append(">");
            }

            // JSON ending charachter
            stringBuilder.append("</").append(className).append(">");
        }
        return stringBuilder.toString();
    }

    private static String getNestedClassNameFromSchema(Schema schema) {

        String nestedClassName = schema.getItems().getType() == null ? schema.getItems().get$ref() : schema.getItems().getType();
        nestedClassName = getClassNameFromReference(nestedClassName);
        return nestedClassName;
    }

    private static String getClassNameFromReference(String reference) {
        return reference.contains("/") ? reference.substring(reference.lastIndexOf("/") + 1) : reference;
    }
}
