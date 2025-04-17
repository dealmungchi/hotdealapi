package kr.co.dealmungchi.hotdealapi.controller;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import kr.co.dealmungchi.hotdealapi.service.DealCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/hotdeals/{hotDealId}/comments")
@RequiredArgsConstructor
public class DealCommentController {
    private final DealCommentService commentService;
    
    @GetMapping
    public Mono<ApiResponse<List<DealComment>>> getCommentsByHotDealId(@PathVariable Long hotDealId) {
        return commentService.getCommentsByHotDealId(hotDealId)
                .map(ApiResponse::success);
    }
    
    @PostMapping
    public Mono<ApiResponse<DealComment>> createComment(
            @PathVariable Long hotDealId,
            @RequestBody CommentRequest request) {
        
        DealComment comment = DealComment.builder()
                .hotdealsId(hotDealId)
                .parentId(request.parentId())
                .userId(request.userId())
                .content(request.content())
                .isDeleted(false)
                .build();
                
        return commentService.createComment(comment)
                .map(ApiResponse::success);
    }
    
    public record CommentRequest(Long parentId, Long userId, String content) {}
}