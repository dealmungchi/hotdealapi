package kr.co.dealmungchi.hotdealapi.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.common.response.ErrorResponse;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFound(ResponseStatusException ex) {
  if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND);

    return ResponseEntity
        .status(errorResponse.status())
        .contentType(MediaType.APPLICATION_JSON)
        .body(ApiResponse.failure(errorResponse));
  }

  String code = ex.getStatusCode().is4xxClientError() ? "CLIENT_ERROR"
      : ex.getStatusCode().is5xxServerError() ? "SERVER_ERROR" : "UNKNOWN_ERROR";

  return ResponseEntity
      .status(ex.getStatusCode())
      .contentType(MediaType.APPLICATION_JSON)
      .body(ApiResponse.failure(
          new ErrorResponse(ex.getStatusCode().value(), code, ex.getReason())));
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
  ErrorResponse errorResponse = ErrorResponse.of(ex.getErrorCode());

  return ResponseEntity
      .status(errorResponse.status())
      .contentType(MediaType.APPLICATION_JSON)
      .body(ApiResponse.failure(errorResponse));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
  ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

  return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .contentType(MediaType.APPLICATION_JSON)
      .body(ApiResponse.failure(errorResponse));
  }
}