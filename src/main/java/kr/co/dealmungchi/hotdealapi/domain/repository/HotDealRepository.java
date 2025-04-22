package kr.co.dealmungchi.hotdealapi.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * HotDeal 엔티티에 대한 리포지토리 인터페이스
 * 기본적인 CRUD 작업과 간단한 쿼리를 처리합니다.
 * 복잡한 쿼리는 HotDealRepositoryCustom에 정의되어 있습니다.
 */
@Repository
public interface HotDealRepository extends ReactiveCrudRepository<HotDeal, Long>, HotDealRepositoryCustom {
  
  /**
   * ID 기준 내림차순으로 정렬된 핫딜 목록을 페이지네이션하여 조회합니다.
   */
  Flux<HotDeal> findAllByOrderByIdDesc(Pageable pageable);
  
  /**
   * 특정 프로바이더의 핫딜을 조회합니다.
   */
  Mono<HotDeal> findByIdAndProviderId(Long id, Long providerId);
  
  /**
   * 조회수를 1 증가시킵니다.
   */
  @Modifying
  @Query("UPDATE hotdeals SET view_count = view_count + 1 WHERE id = :id")
  Mono<Integer> incrementViewCount(Long id);
}