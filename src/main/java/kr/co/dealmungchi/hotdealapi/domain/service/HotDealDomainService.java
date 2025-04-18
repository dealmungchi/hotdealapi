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