package it.gov.pagopa.mockconfig.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum AppError {


  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something went wrong."),
  MOCK_RESOURCE_BAD_REQUEST_INVALID_RESOURCE_ID(HttpStatus.BAD_REQUEST, "Invalid mock resource identifier", "The passed resource id with value [%s] must be equals to the one defined from request ([%s])."),
  MOCK_RESOURCE_CONFLICT(HttpStatus.CONFLICT, "Mock resource already exists", "Another mock resource exists with id [%s]."),
  MOCK_RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Mock resource not found", "No valid mock resource found with id [%s].")
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


