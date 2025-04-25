package kr.co.dealmungchi.hotdealapi.domain.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import kr.co.dealmungchi.hotdealapi.common.exception.DomainException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.domain.repository.HotDealRepository;
import kr.co.dealmungchi.hotdealapi.domain.repository.ProviderRepository;
import kr.co.dealmungchi.hotdealapi.dto.HotDealSearchSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 핫딜 도메인 서비스
 * 도메인 로직과 리포지토리 계층 사이의 중간 계층으로, 도메인 관련 비즈니스 로직을 구현합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HotDealDomainService {
	private final HotDealRepository hotDealRepository;
	private final ProviderRepository hotDealProviderRepository;

	/**
	 * Spec DTO를 사용하여 핫딜 목록 조회
	 * 
	 * @param spec 검색 조건
	 * @return 핫딜 목록
	 */
	public Flux<HotDeal> findHotDealsBySpec(HotDealSearchSpec spec) {
		return hotDealRepository.findBySpec(spec)
				.flatMapSequential(this::attachProvider);
	}
	
	/**
	 * Spec DTO를 사용하여 마지막 ID 이후 더 많은 아이템이 있는지 확인
	 * 
	 * @param spec 검색 조건
	 * @param lastId 마지막 ID
	 * @return 더 많은 데이터 존재 여부
	 */
	public Mono<Boolean> hasMoreItemsBySpec(HotDealSearchSpec spec, Long lastId) {
		if (lastId == null) {
			return Mono.just(false);
		}
		return hotDealRepository.hasMoreItems(spec, lastId);
	}

	/**
	 * 페이지네이션을 사용하여 핫딜 목록 조회
	 * 
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @return 페이지네이션된 핫딜 목록
	 */
	public Flux<HotDeal> findHotDealsWithPagination(int page, int size) {
		return hotDealRepository.findAllByOrderByIdDesc(PageRequest.of(page, size))
				.flatMap(this::attachProvider);
	}

	/**
	 * ID로 핫딜 조회
	 * 
	 * @param id 핫딜 ID
	 * @return 핫딜 정보
	 * @throws DomainException 핫딜이 존재하지 않을 경우
	 */
	public Mono<HotDeal> findHotDealById(Long id) {
		return hotDealRepository.findById(id)
				.switchIfEmpty(Mono.error(new DomainException(ErrorCode.HOTDEAL_NOT_FOUND)))
				.flatMap(this::attachProvider);
	}

	/**
	 * 조회수 증가
	 * 
	 * @param id 핫딜 ID
	 * @return 업데이트된 핫딜 정보
	 */
	public Mono<HotDeal> incrementViewCount(Long id) {
		return hotDealRepository.incrementViewCount(id)
				.then(findHotDealById(id));
	}

	/**
	 * 핫딜에 프로바이더 정보 추가
	 * 
	 * @param hotDeal 핫딜 엔티티
	 * @return 프로바이더 정보가 추가된 핫딜 엔티티
	 */
	private Mono<HotDeal> attachProvider(HotDeal hotDeal) {
		return hotDealProviderRepository.findById(hotDeal.getProviderId())
				.map(provider -> {
					hotDeal.setProvider(provider);
					return hotDeal;
				})
				.defaultIfEmpty(hotDeal);
	}
}