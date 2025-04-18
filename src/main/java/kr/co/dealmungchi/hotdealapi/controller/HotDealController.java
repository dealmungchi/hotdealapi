package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
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
	public Mono<ApiResponse<List<HotDealDto>>> getHotDeals(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return hotDealService.getHotDealsWithPagination(page, size)
				.map(HotDealDto::fromEntity)
				.collectList()
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