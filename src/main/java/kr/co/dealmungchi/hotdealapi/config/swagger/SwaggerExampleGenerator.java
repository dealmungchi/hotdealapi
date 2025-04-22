package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;

/**
 * Utility class for generating example error responses for Swagger documentation.
 */
public class SwaggerExampleGenerator {

    private static final DateTimeFormatter DATETIME_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Generates an example ApiResponse with error for API documentation
     */
    public static Map<String, Object> generateErrorApiResponseExample(ErrorCode errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", null);
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", errorCode.getStatus());
        error.put("code", errorCode.getCode());
        error.put("message", errorCode.getMessage());
        
        response.put("error", error);
        response.put("localDateTime", LocalDateTime.now().format(DATETIME_FORMATTER));
        return response;
    }
}