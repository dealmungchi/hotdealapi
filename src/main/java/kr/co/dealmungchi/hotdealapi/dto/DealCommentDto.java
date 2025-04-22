package kr.co.dealmungchi.hotdealapi.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import lombok.Builder;

@Builder
public record DealCommentDto(
    @Schema(description = "댓글 ID", example = "1")
    Long id,
    @Schema(description = "핫딜 ID", example = "1")
    Long hotdealsId,
    @Schema(description = "부모 댓글 ID", example = "1")
    Long parentId,
    @Schema(description = "사용자 ID", example = "1")
    Long userId,
    @Schema(description = "댓글 내용", example = "댓글 내용")
    String content,
    @Schema(description = "대댓글 목록", example = "[]")
    List<DealCommentDto> replies,
    @Schema(description = "댓글 삭제 여부", example = "false")
    Boolean isDeleted,
    @Schema(description = "댓글 생성일", example = "2025-01-01 10:00:00")
    LocalDateTime createdAt,
    @Schema(description = "댓글 수정일", example = "2025-01-01 10:00:00")
    LocalDateTime updatedAt) {
  public static DealCommentDto fromEntity(DealComment entity) {
    return DealCommentDto.builder()
        .id(entity.getId())
        .hotdealsId(entity.getHotdealsId())
        .parentId(entity.getParentId())
        .userId(entity.getUserId())
        .content(entity.getContent())
        .replies(entity.getReplies().stream()
            .map(DealCommentDto::fromEntity)
            .toList())
        .isDeleted(entity.getIsDeleted())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}