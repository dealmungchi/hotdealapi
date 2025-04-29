package kr.co.dealmungchi.hotdealapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.dealmungchi.hotdealapi.domain.entity.Provider;
import kr.co.dealmungchi.hotdealapi.domain.entity.Provider.ProviderType;
import lombok.Builder;

@Builder
@Schema(description = "핫딜 제공자 정보")
public record ProviderDto(
    @Schema(description = "프로바이더 ID", example = "1")
    Long id,
    
    @Schema(description = "프로바이더 표시 이름", example = "어미새")
    String displayName
) {
    public static ProviderDto fromEntity(Provider entity) {
        return ProviderDto.builder()
                .id(entity.getId())
                .displayName(getDisplayName(entity.getProviderType()))
                .build();
    }
    
    private static String getDisplayName(ProviderType providerType) {
        return switch (providerType) {
            case ARCA -> "아카라이브";
            case CLIEN -> "클리앙";
            case COOLANDJOY -> "쿨앤조이";
            case DAMOANG -> "다모아";
            case FMKOREA -> "에펨코리아";
            case PPOMPPU -> "뽐뿌";
            case PPOMPPUEN -> "뽐뿌해외";
            case QUASAR -> "퀘이사존";
            case RULIWEB -> "루리웹";
            case DEALBADA -> "딜바다";
            case MISSYCOUPONS -> "미시쿠폰";
            case MALLTAIL -> "몰테일";
            case BBASAK -> "빠삭";
            case CITY -> "시티";
            case EOMISAE -> "어미새";
            case ZOD -> "조드";
        };
    }
} 