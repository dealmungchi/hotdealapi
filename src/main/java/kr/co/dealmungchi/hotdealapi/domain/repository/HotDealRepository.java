package kr.co.dealmungchi.hotdealapi.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface HotDealRepository extends ReactiveCrudRepository<HotDeal, Long> {
  Flux<HotDeal> findAllByOrderByIdDesc(Pageable pageable);
  
  @Query("SELECT * FROM hotdeals WHERE id < :cursor ORDER BY id DESC LIMIT :limit")
  Flux<HotDeal> findByIdLessThanOrderByIdDesc(Long cursor, int limit);
  
  @Query("SELECT EXISTS(SELECT 1 FROM hotdeals WHERE id < :lastId LIMIT 1) as has_more")
  Mono<Integer> checkExistsByIdLessThan(Long lastId);
  
  Mono<HotDeal> findByIdAndProviderId(Long id, Long providerId);
  
  @Query("SELECT * FROM hotdeals WHERE provider_id = :providerId ORDER BY id DESC LIMIT :limit")
  Flux<HotDeal> findByProviderIdOrderByIdDesc(Long providerId, int limit);
  
  @Query("SELECT * FROM hotdeals WHERE id < :cursor AND provider_id = :providerId ORDER BY id DESC LIMIT :limit")
  Flux<HotDeal> findByIdLessThanAndProviderIdOrderByIdDesc(Long cursor, Long providerId, int limit);
  
  @Query("SELECT EXISTS(SELECT 1 FROM hotdeals WHERE id < :lastId AND provider_id = :providerId LIMIT 1) as has_more")
  Mono<Integer> checkExistsByIdLessThanAndProviderId(Long lastId, Long providerId);
  
  @Modifying
  @Query("UPDATE hotdeals SET view_count = view_count + 1 WHERE id = :id")
  Mono<Integer> incrementViewCount(Long id);
}