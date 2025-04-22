package kr.co.dealmungchi.hotdealapi.service;

import org.springframework.stereotype.Service;

import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.service.HotDealDomainService;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;
import kr.co.dealmungchi.hotdealapi.dto.HotDealSearchSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 핫딜 서비스
 * 컨트롤러와 도메인 서비스 사이의 중간 계층으로, 비즈니스 로직과 예외 처리를 담당합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HotDealService {
	private final HotDealDomainService hotDealDomainService;

	/**
	 * HotDealSearchSpec을 사용하여 핫딜 목록을 조회합니다.
	 * 
	 * @param spec 검색 조건
	 * @return 핫딜 목록 응답
	 */
	public Mono<HotDealListResponse> getHotDealsWithSpec(HotDealSearchSpec spec) {
		try {
			return hotDealDomainService.findHotDealsBySpec(spec)
					.map(HotDealDto::fromEntity)
					.collectList()
					.flatMap(hotDeals -> {
						if (hotDeals.isEmpty()) {
							return Mono.just(HotDealListResponse.of(hotDeals, true, spec.getSize()));
						}

						Long lastId = hotDeals.get(hotDeals.size() - 1).id();
						return hotDealDomainService.hasMoreItemsBySpec(spec, lastId)
								.map(hasMore -> HotDealListResponse.of(hotDeals, !hasMore, spec.getSize()));
					});
		} catch (Exception e) {
			log.error("Failed to get hot deals with spec: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}

	/**
	 * ID로 핫딜 정보를 조회합니다.
	 * 
	 * @param id 핫딜 ID
	 * @return 핫딜 정보
	 */
	public Mono<HotDealDto> getHotDealById(Long id) {
		try {
			if (id == null || id <= 0) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 ID입니다."));
			}

			return hotDealDomainService.findHotDealById(id)
					.map(HotDealDto::fromEntity);
		} catch (Exception e) {
			log.error("Failed to get hot deal by id: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}

	/**
	 * 핫딜 조회수를 증가시킵니다.
	 * 
	 * @param id 핫딜 ID
	 * @return 업데이트된 핫딜 정보
	 */
	public Mono<HotDealDto> incrementViewCount(Long id) {
		try {
			if (id == null || id <= 0) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 ID입니다."));
			}

			return hotDealDomainService.incrementViewCount(id)
					.map(HotDealDto::fromEntity);
		} catch (Exception e) {
			log.error("Failed to increment view count: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}
}