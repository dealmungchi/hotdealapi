package kr.co.dealmungchi.hotdealapi.domain.repository;

import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DealCommentRepository extends ReactiveCrudRepository<DealComment, Long> {
    Flux<DealComment> findByHotdealsIdAndParentIdIsNullOrderByCreatedAtAsc(Long hotdealsId);
    
    Flux<DealComment> findByParentIdOrderByCreatedAtAsc(Long parentId);
    
    @Query("SELECT * FROM deal_comments WHERE hotdeals_id = :hotdealsId ORDER BY created_at ASC")
    Flux<DealComment> findAllCommentsByHotdealId(Long hotdealsId);
    
    Mono<Boolean> existsByParentId(Long parentId);
}