package kr.co.dealmungchi.hotdealapi.dto;

import java.time.LocalDateTime;

import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.domain.entity.Provider.ProviderType;
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
        String thumbnailLink = null;
        if (entity.getThumbnailHash() != null && !ProviderType.BBASAK.equals(entity.getProvider().getProviderType())) { // TODO : worker 에서 레퍼러 설정으로 가능한지 확인
            thumbnailLink = System.getenv("STATIC_URL") + "/thumbnail/" + entity.getThumbnailHash().substring(0, 2) + "/" + entity.getThumbnailHash();
        }

        return HotDealDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .link(entity.getLink())
                .thumbnailLink(thumbnailLink)
                .price(entity.getPrice())
                .providerId(entity.getProviderId())
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
} 