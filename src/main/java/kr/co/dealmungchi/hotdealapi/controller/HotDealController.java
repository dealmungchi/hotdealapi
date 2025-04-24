package kr.co.dealmungchi.hotdealapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;
import kr.co.dealmungchi.hotdealapi.dto.HotDealSearchSpec;
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
			@RequestParam(required = false) Long cursor,
			@RequestParam(required = false, name = "provider_id") Long providerId,
			@RequestParam(required = false) String keyword) {
		
		// HotDealSearchSpec 생성
		HotDealSearchSpec searchSpec = HotDealSearchSpec.builder()
				.cursor(cursor)
				.size(size)
				.providerId(providerId)
				.keyword(keyword)
				.build();
		
		// 유효성 검사 및 처리
		try {
			searchSpec.validate();
			return hotDealService.getHotDealsWithSpec(searchSpec)
					.map(ApiResponse::success);
		} catch (IllegalArgumentException e) {
			return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, e.getMessage()));
		}
	}

	@Override
	@GetMapping("/{id}")
	public Mono<ApiResponse<HotDealDto>> getHotDealById(@PathVariable Long id) {
		return hotDealService.getHotDealById(id)
				.map(ApiResponse::success);
	}

	@Override
	@PatchMapping("/{id}/view-count")
	public Mono<ApiResponse<HotDealDto>> incrementViewCount(@PathVariable Long id) {
		return hotDealService.incrementViewCount(id)
				.map(ApiResponse::success);
	}
}