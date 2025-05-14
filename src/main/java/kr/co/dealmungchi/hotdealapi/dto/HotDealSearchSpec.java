package kr.co.dealmungchi.hotdealapi.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 핫딜 검색에 필요한 조건을 담는 Spec DTO
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
@Getter
@Setter
@Schema(description = "핫딜 검색 조건")
public class HotDealSearchSpec {
    
    @Parameter(description = "커서 ID (페이징에 사용): null이면 최신순 조회", example = "1000")
    private Long cursor;
    
    @Parameter(description = "페이지 크기 (1-30)", schema = @Schema(minimum = "1", maximum = "30"), example = "20")
    private Integer size;
    
    @Parameter(description = "제공자 ID 목록 (선택 사항, 콤마로 구분): 필터링에 사용", example = "1,2,3")
    private java.util.List<Long> providerIds;
    
    @Parameter(description = "카테고리 ID 목록 (선택 사항, 콤마로 구분): 필터링에 사용", example = "1,2,3")
    private java.util.List<Long> categoryIds;
    
    @Parameter(description = "검색 키워드 (선택 사항): 제목 내 포함 검색", example = "노트북")
    private String keyword;
    
    public void validate() {
        if (size == null || size < 1 || size > 30) {
            throw new IllegalArgumentException("Invalid size: " + size);
        }
    }
    
    public boolean hasCursor() {
        return cursor != null && cursor > 0;
    }
    
    public boolean hasProviderFilter() {
        return providerIds != null && !providerIds.isEmpty();
    }
    
    public boolean hasCategoryFilter() {
        return categoryIds != null && !categoryIds.isEmpty();
    }
    
    public boolean hasKeyword() {
        return keyword != null && !keyword.isBlank();
    }
} 