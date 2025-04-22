package kr.co.dealmungchi.hotdealapi.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "핫딜 목록 응답")
public record HotDealListResponse(
    @Schema(description = "핫딜 목록") 
    List<HotDealDto> content,
    
    @Schema(description = "마지막 페이지 여부", example = "false") 
    boolean last,
    
    @Schema(description = "페이지당 항목 수", example = "5") 
    int size,
    
    @Schema(description = "다음 요청에 사용할 커서 ID (마지막 아이템의 ID)", example = "42") 
    Long nextCursor
) {
    public static HotDealListResponse of(List<HotDealDto> content, boolean last, int size) {
        Long nextCursor = null;
        if (!content.isEmpty() && !last) {
            nextCursor = content.get(content.size() - 1).id();
        }
        
        return HotDealListResponse.builder()
                .content(content)
                .last(last)
                .size(size)
                .nextCursor(nextCursor)
                .build();
    }
}