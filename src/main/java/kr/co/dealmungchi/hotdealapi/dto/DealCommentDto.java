package kr.co.dealmungchi.hotdealapi.dto;

import java.time.LocalDateTime;

import kr.co.dealmungchi.hotdealapi.domain.entity.DealComment;
import lombok.Builder;

@Builder
public record DealCommentDto(
    Long id,
    Long hotdealsId,
    Long parentId,
    Long userId,
    String content,
    Boolean isDeleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static DealCommentDto fromEntity(DealComment entity) {
        return DealCommentDto.builder()
                .id(entity.getId())
                .hotdealsId(entity.getHotdealsId())
                .parentId(entity.getParentId())
                .userId(entity.getUserId())
                .content(entity.getContent())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
} 