package kr.co.dealmungchi.hotdealapi.domain.repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.dto.HotDealSearchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * HotDeal 엔티티에 대한 커스텀 리포지토리 인터페이스
 * 복잡한 검색 조건이나 동적 쿼리가 필요한 메소들을 정의합니다.
 */
public interface HotDealRepositoryCustom {

  /**
   * 검색 조건(Spec)에 따라 핫딜 목록을 조회합니다.
   * 
   * @param spec 검색 조건 (cursor, size, providerId 등)
   * @return 검색 조건에 맞는 핫딜 목록
   */
  Flux<HotDeal> findBySpec(HotDealSearchSpec spec);

  /**
   * 검색 조건(Spec)과 마지막 ID 기준으로 다음 페이지가 존재하는지 확인합니다.
   * 
   * @param spec   검색 조건
   * @param lastId 마지막으로 조회된 핫딜 ID
   * @return 다음 페이지 존재 여부 (true/false)
   */
  Mono<Boolean> hasMoreItems(HotDealSearchSpec spec, Long lastId);
}