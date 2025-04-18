package kr.co.dealmungchi.hotdealapi.common.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Schema Documentation class for API responses.
 * This class is for Swagger documentation purposes only and is not used in the actual application.
 */
public class ApiResponseDocs {

    /**
     * A successful API response with data.
     *
     * @param <T> The type of data contained in the response.
     */
    @Schema(description = "Standard API success response")
    public static class Success<T> {
        @Schema(description = "The response data")
        private T data;

        @Schema(description = "Error information (null for success responses)")
        private Object error = null;

        @Schema(description = "Response timestamp", example = "2023-01-01 12:00:00")
        private String localDateTime;
    }

    /**
     * A failed API response with error details.
     */
    @Schema(description = "Standard API error response")
    public static class Error {
        @Schema(description = "The response data (null for error responses)")
        private Object data = null;

        @Schema(description = "Error information")
        private ErrorResponse error;

        @Schema(description = "Response timestamp", example = "2023-01-01 12:00:00")
        private String localDateTime;
    }
}