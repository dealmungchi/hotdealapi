package kr.co.dealmungchi.hotdealapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;
import kr.co.dealmungchi.hotdealapi.service.HotDealService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hotdeals")
@RequiredArgsConstructor
public class HotDealController implements HotDealControllerSpec {
	private final HotDealService hotDealService;

	@Override
	@GetMapping
	public Mono<ApiResponse<HotDealListResponse>> getHotDeals(
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(required = false) Long cursor) {
		return hotDealService.getHotDealsWithCursor(size, cursor)
				.map(ApiResponse::success);
	}

	@Override
	@GetMapping("/{id}")
	public Mono<ApiResponse<HotDealDto>> getHotDealById(@PathVariable Long id) {
		return hotDealService.getHotDealById(id)
				.map(HotDealDto::fromEntity)
				.map(ApiResponse::success);
	}

	@Override
	@PatchMapping("/{id}/view-count")
	public Mono<ApiResponse<HotDealDto>> incrementViewCount(@PathVariable Long id) {
		return hotDealService.incrementViewCount(id)
				.map(HotDealDto::fromEntity)
				.map(ApiResponse::success);
	}
}