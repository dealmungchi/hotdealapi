package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.dto.DealCommentDto;
import reactor.core.publisher.Mono;

@Tag(name = "Deal Comments", description = "Deal Comments API")
public interface DealCommentControllerSpec {

	Mono<ApiResponse<List<DealCommentDto>>> getCommentsByHotDealId(
			@Parameter(description = "ID of the hot deal") @PathVariable Long hotDealId);

	Mono<ApiResponse<DealCommentDto>> createComment(
			@Parameter(description = "ID of the hot deal") @PathVariable Long hotDealId,
			@Parameter(description = "Comment details", schema = @Schema(implementation = DealCommentController.CommentRequest.class)) @RequestBody DealCommentController.CommentRequest request);
}