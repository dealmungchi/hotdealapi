package kr.co.dealmungchi.hotdealapi.domain.service;

import kr.co.dealmungchi.hotdealapi.common.exception.DomainException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import kr.co.dealmungchi.hotdealapi.domain.repository.DealCommentRepository;
import kr.co.dealmungchi.hotdealapi.domain.repository.HotDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DealCommentDomainService {
    private final DealCommentRepository dealCommentRepository;
    private final HotDealRepository hotDealRepository;
    
    public Mono<List<DealComment>> findCommentsByHotDealId(Long hotDealId) {
        return hotDealRepository.findById(hotDealId)
                .switchIfEmpty(Mono.error(new DomainException(ErrorCode.HOTDEAL_NOT_FOUND)))
                .flatMapMany(hotDeal -> dealCommentRepository.findAllCommentsByHotdealId(hotDealId))
                .collectList()
                .map(this::organizeCommentsHierarchy);
    }
    
    public Mono<DealComment> createComment(DealComment comment) {
        return hotDealRepository.findById(comment.getHotdealsId())
                .switchIfEmpty(Mono.error(new DomainException(ErrorCode.HOTDEAL_NOT_FOUND)))
                .then(validateParentComment(comment))
                .then(dealCommentRepository.save(comment));
    }
    
    private Mono<Void> validateParentComment(DealComment comment) {
        if (comment.getParentId() == null) {
            return Mono.empty();
        }
        
        return dealCommentRepository.findById(comment.getParentId())
                .switchIfEmpty(Mono.error(new DomainException(ErrorCode.PARENT_COMMENT_NOT_FOUND)))
                .filter(parent -> parent.getHotdealsId().equals(comment.getHotdealsId()))
                .switchIfEmpty(Mono.error(new DomainException(ErrorCode.INVALID_COMMENT_REQUEST, 
                        "부모 댓글이 다른 핫딜에 속해 있습니다.")))
                .filter(parent -> parent.getParentId() == null)
                .switchIfEmpty(Mono.error(new DomainException(ErrorCode.INVALID_COMMENT_REQUEST, 
                        "대댓글에는 댓글을 달 수 없습니다.")))
                .then();
    }
    
    private List<DealComment> organizeCommentsHierarchy(List<DealComment> allComments) {
        // Group by parent comments
        Map<Long, List<DealComment>> commentsByParentId = allComments.stream()
                .filter(comment -> comment.getParentId() != null)
                .collect(java.util.stream.Collectors.groupingBy(DealComment::getParentId));
        
        // Get root comments (no parent)
        List<DealComment> rootComments = allComments.stream()
                .filter(comment -> comment.getParentId() == null)
                .collect(java.util.stream.Collectors.toList());
        
        // Attach replies to each root comment
        rootComments.forEach(root -> {
            List<DealComment> replies = commentsByParentId.getOrDefault(root.getId(), List.of());
            root.setReplies(replies);
        });
        
        return rootComments;
    }
}