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
import it.gov.pagopa.mockconfig.model.mockresource.MockResource;
import it.gov.pagopa.mockconfig.model.mockresource.MockResourceList;
import it.gov.pagopa.mockconfig.service.MockResourceService;
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
@RequestMapping(path = "/resources")
@Tag(name = "Mock Resources", description = "Everything about Mock Resources")
@Validated
public class MockResourceController {

    @Autowired private ModelMapper modelMapper;

    @Autowired
    private MockResourceService mockResourceService;

    @Operation(
            summary = "Get paginated list of mock resource",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Mock Resources"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MockResourceList.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MockResourceList> getMockResources(
            @Parameter(description = "The number of elements to be included in the page.", required = true)
            @Valid @RequestParam(required = false, defaultValue = "10") @Positive @Max(999) Integer limit,
            @Parameter(description = "The index of the page, starting from 0.", required = true)
            @Valid @Min(0) @RequestParam(required = false, defaultValue = "0") Integer page) {
        return ResponseEntity.ok(mockResourceService.getMockResources(PageRequest.of(page, limit)));
    }


    @Operation(
            summary = "Get detail of a single mock resource",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Mock Resources"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MockResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "/{resourceId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MockResource> getMockResource(
            @Parameter(description = "The identifier related to the mock resource", required = true)
            @NotBlank @PathVariable("resourceId") Long resourceId) {
        return ResponseEntity.ok(mockResourceService.getMockResource(resourceId));
    }

    @Operation(
            summary = "Create a new mock resource",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Mock Resources"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MockResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MockResource> createMockResource(
            @RequestBody @Valid @NotNull MockResource mockResource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mockResourceService.createMockResource(mockResource));
    }

    @Operation(
            summary = "Update an existing mock resource",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Mock Resources"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MockResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @PutMapping(value = "/{resourceId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MockResource> updateMockResource(
            @Parameter(description = "The identifier related to the mock resource", required = true)
            @NotBlank @PathVariable("resourceId") Long resourceId,
            @RequestBody @Valid @NotNull MockResource mockResource) {
        return ResponseEntity.ok(mockResourceService.updateMockResource(resourceId, mockResource));
    }

    @Operation(
            summary = "Delete an existing mock resource",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Mock Resources"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MockResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ProblemJson.class)))
    })
    @DeleteMapping(value = "/{resourceId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteMockResource(
            @Parameter(description = "The identifier related to the mock resource", required = true)
            @NotBlank @PathVariable("resourceId") Long resourceId) {
        mockResourceService.deleteMockResource(resourceId);
        return ResponseEntity.ok().build();
    }
}
