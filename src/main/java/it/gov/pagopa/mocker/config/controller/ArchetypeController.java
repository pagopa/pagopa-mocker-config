package it.gov.pagopa.mocker.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.mocker.config.model.ProblemJson;
import it.gov.pagopa.mocker.config.model.archetype.Archetype;
import it.gov.pagopa.mocker.config.model.archetype.ArchetypeHandlingResult;
import it.gov.pagopa.mocker.config.model.archetype.ArchetypeList;
import it.gov.pagopa.mocker.config.model.archetype.MockResourceFromArchetype;
import it.gov.pagopa.mocker.config.model.mockresource.MockResource;
import it.gov.pagopa.mocker.config.service.ArchetypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArchetypeHandlingResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PostMapping(value = "/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArchetypeHandlingResult> importArchetypesFromOpenAPI(
            @Parameter()
            @NotBlank @RequestParam("subsystem") String subsystem,
            @Parameter(description = "JSON file containing the OpenAPI to analyze", required = true, content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @NotNull @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(archetypeService.importArchetypesFromOpenAPI(file, subsystem));
    }

    @Operation(
            summary = "Get paginated list of archetypes",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArchetypeList.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArchetypeList> getArchetypes(
            @Parameter(description = "The number of elements to be included in the page.", required = true)
            @Valid @RequestParam(required = false, defaultValue = "10") @Positive @Max(999) Integer limit,
            @Parameter(description = "The index of the page, starting from 0.", required = true)
            @Valid @Min(0) @RequestParam(required = false, defaultValue = "0") Integer page) {
        return ResponseEntity.ok(archetypeService.getArchetypes(PageRequest.of(page, limit)));
    }

    @Operation(
            summary = "Get detail of a single archetype",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Archetype.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "/{archetypeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Archetype> getArchetype(
            @Parameter(description = "The identifier related to the archetype", required = true)
            @NotBlank @PathVariable("archetypeId") String archetypeId) {
        return ResponseEntity.ok(archetypeService.getArchetype(archetypeId));
    }


    @Operation(
            summary = "Create a new archetype",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Archetype.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Archetype> createArchetype(
            @RequestBody @Valid @NotNull Archetype archetype) {
        return ResponseEntity.status(HttpStatus.CREATED).body(archetypeService.createArchetype(archetype));
    }

    @Operation(
            summary = "Update an existing archetype",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Archetype.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PutMapping(value = "/{archetypeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Archetype> updateArchetype(
            @Parameter(description = "The identifier related to the archetype", required = true)
            @NotBlank @PathVariable("archetypeId") String archetypeId,
            @RequestBody @Valid @NotNull Archetype archetype) {
        return ResponseEntity.ok(archetypeService.updateArchetype(archetypeId, archetype));
    }

    @Operation(
            summary = "Delete all existing archetypes by criteria",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @DeleteMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArchetypeHandlingResult> deleteMockResource(
            @Parameter()
            @NotBlank @RequestParam("subsystem") String subsystem) {
        return ResponseEntity.ok(archetypeService.deleteByCriteria(subsystem));
    }

    @Operation(
            summary = "Create a new mock resource starting from archetype",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Archetypes"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MockResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PostMapping(value = "/{archetypeId}/generate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MockResource> createMockResourceFromArchetype(
            @Parameter(description = "The identifier related to the archetype", required = true)
            @NotBlank @PathVariable("archetypeId") String archetypeId,
            @RequestBody @Valid @NotNull MockResourceFromArchetype mockResourceFromArchetype) {
        return ResponseEntity.status(HttpStatus.CREATED).body(archetypeService.createMockResourceFromArchetype(archetypeId, mockResourceFromArchetype));
    }
}
