package kr.co.dealmungchi.hotdealapi.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 핫딜 검색에 필요한 조건을 담는 Spec DTO
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
public class HotDealSearchSpec {
    
    /**
     * 커서 기반 페이징을 위한 기준점 ID
     */
    private final Long cursor;
    
    /**
     * 페이지 사이즈 (기본값: 5)
     */
    private final int size;
    
    /**
     * 프로바이더 ID 필터 (옵션)
     */
    private final Long providerId;
    
    /**
     * 유효성 검사
     */
    public HotDealSearchSpec validate() {
        if (size <= 0 || size > 30) {
            throw new IllegalArgumentException("Size must be between 1 and 30");
        }
        return this;
    }
    
    /**
     * 커서가 있는지 확인
     */
    public boolean hasCursor() {
        return cursor != null;
    }
    
    /**
     * Provider 필터가 적용되었는지 확인
     */
    public boolean hasProviderFilter() {
        return providerId != null;
    }
} 