package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.config.swagger.ApiResponseSpec;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import reactor.core.publisher.Mono;

@Tag(name = "Hot Deal", description = "Hot Deal API")
public interface HotDealControllerSpec {

    @ApiResponseSpec(
        responseClass = List.class,
        errorCodes = {ErrorCode.INVALID_REQUEST}
    )
    Mono<ApiResponse<List<HotDealDto>>> getHotDeals(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Size of the page") @RequestParam(defaultValue = "20") int size);

    @ApiResponseSpec(
        responseClass = HotDealDto.class,
        errorCodes = {ErrorCode.HOTDEAL_NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND}
    )
    Mono<ApiResponse<HotDealDto>> getHotDealById(
            @Parameter(description = "ID of the hot deal to retrieve") @PathVariable Long id);

    @ApiResponseSpec(
        responseClass = HotDealDto.class,
        errorCodes = {ErrorCode.HOTDEAL_NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND}
    )
    Mono<ApiResponse<HotDealDto>> incrementViewCount(
            @Parameter(description = "ID of the hot deal") @PathVariable Long id);
}