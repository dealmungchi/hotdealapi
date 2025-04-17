package kr.co.dealmungchi.hotdealapi.service;

import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import kr.co.dealmungchi.hotdealapi.domain.service.DealCommentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealCommentService {
    private final DealCommentDomainService commentDomainService;
    
    public Mono<List<DealComment>> getCommentsByHotDealId(Long hotDealId) {
        try {
            if (hotDealId == null || hotDealId <= 0) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 핫딜 ID입니다."));
            }
            
            return commentDomainService.findCommentsByHotDealId(hotDealId);
        } catch (Exception e) {
            log.error("Failed to get comments for hot deal id {}: {}", hotDealId, e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
    
    public Mono<DealComment> createComment(DealComment comment) {
        try {
            if (comment.getHotdealsId() == null || comment.getHotdealsId() <= 0) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 핫딜 ID입니다."));
            }
            
            if (comment.getContent() == null || comment.getContent().isBlank()) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "댓글 내용은 필수입니다."));
            }
            
            // Set default values
            if (comment.getIsDeleted() == null) {
                comment.setIsDeleted(false);
            }
            
            return commentDomainService.createComment(comment);
        } catch (Exception e) {
            log.error("Failed to create comment: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}