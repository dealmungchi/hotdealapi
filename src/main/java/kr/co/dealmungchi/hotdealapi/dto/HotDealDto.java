package kr.co.dealmungchi.hotdealapi.dto;

import java.time.LocalDateTime;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import lombok.Builder;

@Builder
public record HotDealDto(
    Long id,
    String title,
    String link,
    String thumbnailLink,
    String price,
    Long providerId,
    Long viewCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static HotDealDto fromEntity(HotDeal entity) {
        return HotDealDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .link(entity.getLink())
                .thumbnailLink(entity.getThumbnailLink())
                .price(entity.getPrice())
                .providerId(entity.getProviderId())
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
} 