package kr.co.dealmungchi.hotdealapi.common.exception;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResponseStatusException ex) {
        if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
            ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.RESOURCE_NOT_FOUND.name(),
                "요청한 리소스를 찾을 수 없습니다.");
                
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.error(errorResponse));
        }
        
        String code = ex.getStatusCode().is4xxClientError() ? "CLIENT_ERROR" :
                  ex.getStatusCode().is5xxServerError() ? "SERVER_ERROR" : "UNKNOWN_ERROR";

        return ResponseEntity
            .status(ex.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.error(
                new ErrorResponse(code, ex.getReason())));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode().name(),
            ex.getMessage());
            
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.error(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorCode.INTERNAL_SERVER_ERROR.name(),
            "서버 내부 오류가 발생했습니다.");
            
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.error(errorResponse));
    }
}