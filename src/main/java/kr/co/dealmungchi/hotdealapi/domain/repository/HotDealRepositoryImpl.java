package kr.co.dealmungchi.hotdealapi.domain.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.dto.HotDealSearchSpec;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * HotDeal 엔티티에 대한 커스텀 리포지토리 구현체
 * R2dbcEntityOperations를 사용한 자동 엔티티 매핑 구현
 */
@Repository
@RequiredArgsConstructor
public class HotDealRepositoryImpl implements HotDealRepositoryCustom {

  private final R2dbcEntityOperations entityOperations;

  @Override
  public Flux<HotDeal> findBySpec(HotDealSearchSpec spec) {
    // 동적 조건을 Query 객체로 구성
    Criteria criteria = Criteria.empty();

    if (spec.hasCursor()) {
      criteria = criteria.and("id").lessThan(spec.getCursor());
    }

    if (spec.hasProviderFilter()) {
      criteria = criteria.and("provider_id").is(spec.getProviderId());
    }
    
    if (spec.hasCategoryFilter()) {
      criteria = criteria.and("category_id").is(spec.getCategoryId());
    }

    if (spec.hasKeyword()) {
      criteria = criteria.and("title").like("%" + spec.getKeyword() + "%");
    }

    // Query 객체로 변환
    Query query = Query.query(criteria)
        .sort(Sort.by(Sort.Direction.DESC, "id"))
        .limit(spec.getSize());

    // R2dbcEntityOperations를 사용하여 자동 매핑된 결과 반환
    return entityOperations.select(HotDeal.class)
        .matching(query)
        .all();
  }

  @Override
  public Mono<Boolean> hasMoreItems(HotDealSearchSpec spec, Long lastId) {
    // 동적 조건을 Query 객체로 구성
    Criteria criteria = Criteria.where("id").lessThan(lastId);

    if (spec.hasProviderFilter()) {
      criteria = criteria.and("provider_id").is(spec.getProviderId());
    }

    if (spec.hasCategoryFilter()) {
      criteria = criteria.and("category_id").is(spec.getCategoryId());
    }

    if (spec.hasKeyword()) {
      criteria = criteria.and("title").like("%" + spec.getKeyword() + "%");
    }

    // 결과 존재 여부 확인을 위한 쿼리 생성
    Query query = Query.query(criteria).limit(1);

    // 결과가 존재하는지 확인 (exists 메소드 대신 count를 사용한 후 0보다 큰지 확인)
    return entityOperations.count(query, HotDeal.class)
        .map(count -> count > 0);
  }
}