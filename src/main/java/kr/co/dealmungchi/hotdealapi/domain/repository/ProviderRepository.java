package kr.co.dealmungchi.hotdealapi.domain.repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.Provider;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends ReactiveCrudRepository<Provider, Long> {
}