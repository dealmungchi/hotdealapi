package kr.co.dealmungchi.hotdealapi.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import lombok.Builder;

@Builder
public record HotDealDto(
    @Schema(description = "핫딜 ID", example = "1")
    Long id,
    @Schema(description = "핫딜 제목", example = "핫딜 제목")
    String title,
    @Schema(description = "핫딜 링크", example = "https://www.example.com")
    String link,
    @Schema(description = "핫딜 썸네일 링크", example = "https://www.example.com/thumbnail.jpg")
    String thumbnailLink,
    @Schema(description = "핫딜 가격", example = "10000")
    String price,
    @Schema(description = "핫딜 프로바이더 ID", example = "1")
    Long providerId,
    @Schema(description = "핫딜 카테고리 ID", example = "1")
    Long categoryId,
    @Schema(description = "핫딜 조회수", example = "100")
    Long viewCount,
    @Schema(description = "핫딜 생성일", example = "2025-01-01 10:00:00")
    LocalDateTime createdAt,
    @Schema(description = "핫딜 수정일", example = "2025-01-01 10:00:00")
    LocalDateTime updatedAt
) {
    public static HotDealDto fromEntity(HotDeal entity) {
        String thumbnailLink = null;
        if (entity.getThumbnailHash() != null) {
            thumbnailLink = System.getenv("STATIC_URL") + "/thumbnail/" + entity.getThumbnailHash().substring(0, 2) + "/" + entity.getThumbnailHash();
        }

        return HotDealDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .link(entity.getLink())
                .thumbnailLink(thumbnailLink)
                .price(entity.getPrice())
                .providerId(entity.getProviderId())
                .categoryId(entity.getCategoryId())
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
} 