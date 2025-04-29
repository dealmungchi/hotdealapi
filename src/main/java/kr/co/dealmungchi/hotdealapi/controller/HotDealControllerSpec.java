package kr.co.dealmungchi.hotdealapi.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(
		summary = "Get hot deals list",
		description = "Returns a list of hot deals with pagination, filtering, and search capabilities."
	)
	@ApiResponseSpec(responseClass = HotDealListResponse.class, errorCodes = {
			ErrorCode.INVALID_REQUEST
	})
	Mono<ApiResponse<HotDealListResponse>> getHotDeals(
			@Parameter(description = "Size of the page (1-30)", example = "5") 
			@RequestParam(defaultValue = "5") int size,
			
			@Parameter(description = "Cursor for infinite scroll (last ID from previous page)", example = "42") 
			@RequestParam(required = false) Long cursor,
			
			@Parameter(description = "Filter by provider ID", example = "1") 
			@RequestParam(required = false, name = "provider_id") Long providerId,

			@Parameter(description = "Filter by category ID", example = "1") 
			@RequestParam(required = false) Long categoryId,
			
			@Parameter(description = "Search by keyword in title", example = "노트북") 
			@RequestParam(required = false) String keyword);

	@Operation(
		summary = "Get hot deal by ID",
		description = "Returns detailed information about a specific hot deal by its ID."
	)
	@ApiResponseSpec(responseClass = HotDealDto.class, errorCodes = {
			ErrorCode.HOTDEAL_NOT_FOUND,
	})
	Mono<ApiResponse<HotDealDto>> getHotDealById(
			@Parameter(description = "ID of the hot deal to retrieve", example = "1") 
			@PathVariable Long id);

	@Operation(
		summary = "Increment view count",
		description = "Increments the view count of a hot deal and returns the updated hot deal information."
	)
	@ApiResponseSpec(responseClass = HotDealDto.class, errorCodes = {
			ErrorCode.HOTDEAL_NOT_FOUND,
	})
	Mono<ApiResponse<HotDealDto>> incrementViewCount(
			@Parameter(description = "ID of the hot deal", example = "1") 
			@PathVariable Long id);
}