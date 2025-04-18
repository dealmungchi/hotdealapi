package kr.co.dealmungchi.hotdealapi.service;

import org.springframework.stereotype.Service;

import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.domain.service.HotDealDomainService;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotDealService {
	private final HotDealDomainService hotDealDomainService;

	public Mono<HotDealListResponse> getHotDealsWithCursor(int size, Long cursor) {
		try {
			if (size <= 0 || size > 100) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST,
						"사이즈는 1-100 사이여야 합니다."));
			}

			return hotDealDomainService.findHotDealsWithCursor(cursor, size)
					.map(HotDealDto::fromEntity)
					.collectList()
					.flatMap(hotDeals -> {
						if (hotDeals.isEmpty()) {
							return Mono.just(HotDealListResponse.of(hotDeals, true, size));
						}
						
						Long lastId = hotDeals.get(hotDeals.size() - 1).id();
						return hotDealDomainService.hasMoreItems(lastId)
								.map(hasMore -> HotDealListResponse.of(hotDeals, !hasMore, size));
					});
		} catch (Exception e) {
			log.error("Failed to get hot deals with cursor: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}

	public Mono<HotDeal> getHotDealById(Long id) {
		try {
			if (id == null || id <= 0) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 ID입니다."));
			}

			return hotDealDomainService.findHotDealById(id);
		} catch (Exception e) {
			log.error("Failed to get hot deal by id: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}

	public Mono<HotDeal> incrementViewCount(Long id) {
		try {
			if (id == null || id <= 0) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 ID입니다."));
			}

			return hotDealDomainService.incrementViewCount(id);
		} catch (Exception e) {
			log.error("Failed to increment view count: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}
}