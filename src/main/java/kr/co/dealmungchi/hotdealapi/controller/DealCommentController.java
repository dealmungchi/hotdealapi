package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import kr.co.dealmungchi.hotdealapi.dto.DealCommentDto;
import kr.co.dealmungchi.hotdealapi.service.DealCommentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hotdeals/{hotDealId}/comments")
@RequiredArgsConstructor
public class DealCommentController implements DealCommentControllerSpec {
	private final DealCommentService commentService;

	@Override
	@GetMapping
	public Mono<ApiResponse<List<DealCommentDto>>> getCommentsByHotDealId(@PathVariable Long hotDealId) {
		return commentService.getCommentsByHotDealId(hotDealId)
				.map(comments -> comments.stream()
						.map(DealCommentDto::fromEntity)
						.toList())
				.map(ApiResponse::success);
	}

	@Override
	@PostMapping
	public Mono<ApiResponse<DealCommentDto>> createComment(
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
				.map(DealCommentDto::fromEntity)
				.map(ApiResponse::success);
	}

	public record CommentRequest(Long parentId, Long userId, String content) {
	}
}