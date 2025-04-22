package kr.co.dealmungchi.hotdealapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import kr.co.dealmungchi.hotdealapi.domain.service.DealCommentDomainService;
import kr.co.dealmungchi.hotdealapi.dto.DealCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealCommentService {
	private final DealCommentDomainService commentDomainService;

	public Mono<List<DealCommentDto>> getCommentsByHotDealId(Long hotDealId) {
		try {
			if (hotDealId == null || hotDealId <= 0) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 핫딜 ID입니다."));
			}

			return commentDomainService.findCommentsByHotDealId(hotDealId)
				.map(comments -> comments.stream()
					.map(DealCommentDto::fromEntity)
					.collect(Collectors.toList()));
		} catch (Exception e) {
			log.error("Failed to get comments for hot deal id {}: {}", hotDealId, e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}

	public Mono<DealCommentDto> createComment(Long hotDealId, Long parentId, String content) {
		try {
			if (hotDealId == null || hotDealId <= 0) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 핫딜 ID입니다."));
			}

			if (content == null || content.isBlank()) {
				return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "댓글 내용은 필수입니다."));
			}

			DealComment comment = DealComment.builder()
					.hotdealsId(hotDealId)
					.parentId(parentId)
					.userId(null) // TODO: 로그인 기능 추가 후 수정
					.content(content)
					.isDeleted(false)
					.build();

			return commentDomainService.createComment(comment)
				.map(DealCommentDto::fromEntity);
		} catch (Exception e) {
			log.error("Failed to create comment: {}", e.getMessage(), e);
			return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
		}
	}
}