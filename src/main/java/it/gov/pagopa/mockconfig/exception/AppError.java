package it.gov.pagopa.mockconfig.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum AppError {


  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something went wrong."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_ID(HttpStatus.BAD_REQUEST, "Invalid mock resource identifier", "The passed resource id with value [%s] must be equals to the one defined from request ([%s])."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_URL(HttpStatus.BAD_REQUEST, "Invalid mock resource URL", "The subsystem or the resource URL passed as input is different from the values defined in the resource to be edited and cannot be changed."),
  MOCK_RESOURCE_BAD_REQUEST_DUPLICATE_RULE_ORDER(HttpStatus.BAD_REQUEST, "Duplicated mock rule cardinal order", "One or more cardinal order value for the passed mock rules are duplicated."),
  MOCK_RESOURCE_BAD_REQUEST_DUPLICATE_CONDITION_ORDER(HttpStatus.BAD_REQUEST, "Duplicated mock condition cardinal order", "One or more cardinal order value for the mock condition related to the rules [%s] are duplicated."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_UNARY_CONDITION(HttpStatus.BAD_REQUEST, "Invalid unary condition", "The unary condition for mock rule [%s] at order [%d] is set with a condition value but it must be set with null value."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_BINARY_CONDITION(HttpStatus.BAD_REQUEST, "Invalid binary condition", "The binary condition for mock rule [%] at order [%d] is set without a condition value but it must be set with non-null value."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_REGEX(HttpStatus.BAD_REQUEST, "Invalid regex condition", "The regular expression set in the condition for mock rule [%] at order [%d] is invalid and cannot be used as valid value."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "Invalid content type in condition", "The content type of mock condition for mock rule [%] at order [%d] is set as [%s] but it is incompatible with [%s] content."),
  MOCK_RESOURCE_BAD_REQUEST_UNPARSEABLE_RESPONSE_BODY(HttpStatus.BAD_REQUEST, "Invalid format for response body", "The response body related to the mock rule [%] is not passed as a valid Base64 content."),
  MOCK_RESOURCE_CONFLICT(HttpStatus.CONFLICT, "Mock resource already exists", "Another mock resource exists with id [%s]."),
  MOCK_RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Mock resource not found", "No valid mock resource found with id [%s]."),
  MOCK_RESOURCE_GENERATION_FROM_ARCHETYPE_INVALID_HTTPSTATUS(HttpStatus.BAD_REQUEST, "Invalid HTTP status", "No valid response defined in archetype for the HTTP status [%d]."),
  MOCK_RESOURCE_GENERATION_FROM_ARCHETYPE_MISSING_URL_PARAMETER(HttpStatus.BAD_REQUEST, "Missing URL parameter", "The request must provide all the path parameters defined by the archetype: %s"),
  OPENAPI_IMPORT_INVALID_FILE_CONTENT(HttpStatus.BAD_REQUEST, "Invalid OpenAPI file", "The passed OpenAPI file is invalid or malformed and cannot be analyzed.")
  ;

  public final HttpStatus httpStatus;
  public final String title;
  public final String details;


  AppError(HttpStatus httpStatus, String title, String details) {
    this.httpStatus = httpStatus;
    this.title = title;
    this.details = details;
  }
}


