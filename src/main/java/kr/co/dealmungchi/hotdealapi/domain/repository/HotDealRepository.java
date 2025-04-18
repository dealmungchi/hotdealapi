package kr.co.dealmungchi.hotdealapi.domain.repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface HotDealRepository extends ReactiveCrudRepository<HotDeal, Long> {
  Flux<HotDeal> findAllByOrderByIdDesc(Pageable pageable);
  
  Mono<HotDeal> findByIdAndProviderId(Long id, Long providerId);
  
  @Modifying
  @Query("UPDATE hotdeals SET view_count = view_count + 1 WHERE id = :id")
  Mono<Integer> incrementViewCount(Long id);
}