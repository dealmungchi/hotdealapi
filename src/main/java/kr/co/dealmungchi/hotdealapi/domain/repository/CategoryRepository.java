package kr.co.dealmungchi.hotdealapi.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.Category;
import kr.co.dealmungchi.hotdealapi.domain.entity.Category.CategoryType;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Mono<Category> findByCategoryType(CategoryType categoryType);
} 