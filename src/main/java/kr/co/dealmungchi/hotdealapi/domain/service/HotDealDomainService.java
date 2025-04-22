package kr.co.dealmungchi.hotdealapi.domain.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import kr.co.dealmungchi.hotdealapi.common.exception.DomainException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.domain.repository.HotDealRepository;
import kr.co.dealmungchi.hotdealapi.domain.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HotDealDomainService {
	private final HotDealRepository hotDealRepository;
	private final ProviderRepository hotDealProviderRepository;

	public Flux<HotDeal> findHotDealsWithPagination(int page, int size) {
		return hotDealRepository.findAllByOrderByIdDesc(PageRequest.of(page, size))
				.flatMap(this::attachProvider);
	}

	public Flux<HotDeal> findHotDealsWithCursor(Long cursor, int size) {
		if (cursor == null) {
			return findHotDealsWithPagination(0, size);
		}

		return hotDealRepository.findByIdLessThanOrderByIdDesc(cursor, size)
				.flatMap(this::attachProvider);
	}

	public Flux<HotDeal> findHotDealsWithCursorAndProvider(Long cursor, int size, Long providerId) {
		if (providerId == null) {
			return findHotDealsWithCursor(cursor, size);
		}
		
		if (cursor == null) {
			return hotDealRepository.findByProviderIdOrderByIdDesc(providerId, size)
					.flatMap(this::attachProvider);
		}

		return hotDealRepository.findByIdLessThanAndProviderIdOrderByIdDesc(cursor, providerId, size)
				.flatMap(this::attachProvider);
	}

	public Mono<Boolean> hasMoreItems(Long lastId) {
		if (lastId == null) {
			return Mono.just(false);
		}
		return hotDealRepository.checkExistsByIdLessThan(lastId)
				.map(result -> result > 0);
	}

	public Mono<Boolean> hasMoreItemsWithProvider(Long lastId, Long providerId) {
		if (lastId == null) {
			return Mono.just(false);
		}
		
		if (providerId == null) {
			return hasMoreItems(lastId);
		}
		
		return hotDealRepository.checkExistsByIdLessThanAndProviderId(lastId, providerId)
				.map(result -> result > 0);
	}

	public Mono<HotDeal> findHotDealById(Long id) {
		return hotDealRepository.findById(id)
				.switchIfEmpty(Mono.error(new DomainException(ErrorCode.HOTDEAL_NOT_FOUND)))
				.flatMap(this::attachProvider);
	}

	public Mono<HotDeal> incrementViewCount(Long id) {
		return hotDealRepository.incrementViewCount(id)
				.then(findHotDealById(id));
	}

	private Mono<HotDeal> attachProvider(HotDeal hotDeal) {
		return hotDealProviderRepository.findById(hotDeal.getProviderId())
				.map(provider -> {
					hotDeal.setProvider(provider);
					return hotDeal;
				})
				.defaultIfEmpty(hotDeal);
	}
}