package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import reactor.core.publisher.Mono;

@Tag(name = "Hot Deal", description = "Hot Deal API")
public interface HotDealControllerSpec {

	Mono<ApiResponse<List<HotDealDto>>> getHotDeals(
			@Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Size of the page") @RequestParam(defaultValue = "20") int size);

	Mono<ApiResponse<HotDealDto>> getHotDealById(
			@Parameter(description = "ID of the hot deal to retrieve") @PathVariable Long id);

	Mono<ApiResponse<HotDealDto>> incrementViewCount(
			@Parameter(description = "ID of the hot deal") @PathVariable Long id);
}
