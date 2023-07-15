package it.gov.pagopa.mockconfig.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.mockconfig.model.ProblemJson;
import it.gov.pagopa.mockconfig.model.archetype.ArchetypeCreationResult;
import it.gov.pagopa.mockconfig.model.archetype.MockResourceFromArchetype;
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import it.gov.pagopa.mockconfig.service.ArchetypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;


@RestController()
@RequestMapping(path = "/archetypes")
@Tag(name = "Archetypes", description = "Everything about Archetypes")
@Validated
public class ArchetypeController {

    @Autowired private ModelMapper modelMapper;

    @Autowired
    private ArchetypeService archetypeService;


    @Operation(
            summary = "Create multiple archetypes from OpenAPI specification",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArchetypeCreationResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PostMapping(value = "/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArchetypeCreationResult> importArchetypesFromOpenAPI(
            @Parameter()
            @NotBlank @RequestParam("subsystem") String subsystem,
            @Parameter(description = "JSON file containing the OpenAPI to analyze", required = true, content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @NotNull @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(archetypeService.importArchetypesFromOpenAPI(file, subsystem));
    }


    @PostMapping(value = "/{archetypeId}/generate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MockResource> createMockResourceFromArchetype(
            @Parameter(description = "The identifier related to the archetype", required = true)
            @NotBlank @PathVariable("archetypeId") String archetypeId,
            @RequestBody @Valid @NotNull MockResourceFromArchetype mockResourceFromArchetype) {
        return ResponseEntity.status(HttpStatus.CREATED).body(archetypeService.createMockResourceFromArchetype(archetypeId, mockResourceFromArchetype));
    }
}
