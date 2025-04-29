package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.config.swagger.ApiResponseSpec;
import kr.co.dealmungchi.hotdealapi.dto.CategoryDto;
import kr.co.dealmungchi.hotdealapi.dto.ProviderDto;
import reactor.core.publisher.Mono;

@Tag(name = "Metadata", description = "메타데이터 API")
public interface MetadataControllerSpec {

    @Operation(
        summary = "Get all providers",
        description = "Returns a list of all available providers for hotdeals."
    )
    @ApiResponseSpec(responseClass = List.class, errorCodes = {
            ErrorCode.INTERNAL_SERVER_ERROR
    })
    Mono<ApiResponse<List<ProviderDto>>> getAllProviders();

    @Operation(
        summary = "Get all categories",
        description = "Returns a list of all available categories for hotdeals."
    )
    @ApiResponseSpec(responseClass = List.class, errorCodes = {
            ErrorCode.INTERNAL_SERVER_ERROR
    })
    Mono<ApiResponse<List<CategoryDto>>> getAllCategories();
} 