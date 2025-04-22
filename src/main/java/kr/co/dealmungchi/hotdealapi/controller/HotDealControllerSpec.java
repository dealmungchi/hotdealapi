package kr.co.dealmungchi.hotdealapi.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.config.swagger.ApiResponseSpec;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;
import reactor.core.publisher.Mono;

@Tag(name = "Hot Deal", description = "Hot Deal API")
public interface HotDealControllerSpec {

	@ApiResponseSpec(responseClass = HotDealListResponse.class, errorCodes = {
			ErrorCode.INVALID_REQUEST
	})
	Mono<ApiResponse<HotDealListResponse>> getHotDeals(
			@Parameter(description = "Size of the page") @RequestParam(defaultValue = "20") int size,
			@Parameter(description = "Cursor for infinite scroll (last ID from previous page)") @RequestParam(required = false) Long cursor,
			@Parameter(description = "Filter by provider ID") @RequestParam(required = false, name = "provider_id") Long providerId);

	@ApiResponseSpec(responseClass = HotDealDto.class, errorCodes = {
			ErrorCode.HOTDEAL_NOT_FOUND,
	})
	Mono<ApiResponse<HotDealDto>> getHotDealById(
			@Parameter(description = "ID of the hot deal to retrieve") @PathVariable Long id);

	@ApiResponseSpec(responseClass = HotDealDto.class, errorCodes = {
			ErrorCode.HOTDEAL_NOT_FOUND,
	})
	Mono<ApiResponse<HotDealDto>> incrementViewCount(
			@Parameter(description = "ID of the hot deal") @PathVariable Long id);
}