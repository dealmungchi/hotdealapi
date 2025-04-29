package kr.co.dealmungchi.hotdealapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.dealmungchi.hotdealapi.domain.entity.Category;
import kr.co.dealmungchi.hotdealapi.domain.entity.Category.CategoryType;
import lombok.Builder;

@Builder
@Schema(description = "핫딜 카테고리 정보")
public record CategoryDto(
    @Schema(description = "카테고리 ID", example = "1")
    Long id,
    
    @Schema(description = "카테고리 표시 이름", example = "전자/디지털/PC")
    String displayName
) {
    public static CategoryDto fromEntity(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .displayName(getDisplayName(entity.getCategoryType()))
                .build();
    }
    
    private static String getDisplayName(CategoryType categoryType) {
        return switch (categoryType) {
            case ELECTRONICS_DIGITAL_PC -> "전자제품/디지털/PC/하드웨어";
            case SOFTWARE_GAME -> "소프트웨어/게임";
            case HOUSEHOLD_INTERIOR_KITCHEN -> "생활용품/인테리어/주방";
            case FOOD -> "식품/먹거리";
            case CLOTHING_FASHION_ACCESSORIES -> "의류/패션/잡화";
            case COSMETICS_BEAUTY -> "화장품/뷰티";
            case BOOKS_MEDIA_CONTENTS -> "도서/미디어/콘텐츠";
            case CAMERA_PHOTO -> "카메라/사진";
            case VOUCHER_COUPON_POINT -> "상품권/쿠폰/포인트";
            case BABY_CHILDCARE -> "출산/육아";
            case PET -> "반려동물";
            case SPORTS_OUTDOOR_LEISURE -> "스포츠/아웃도어/레저";
            case HEALTH_VITAMIN -> "건강/비타민";
            case TRAVEL_SERVICE -> "여행/서비스";
            case EVENT_ENTRY_VIRAL -> "이벤트/응모/바이럴";
            case SCHOOL_OFFICE_SUPPLIES -> "학교/사무용품";
            case ETC -> "기타";
        };
    }
} 