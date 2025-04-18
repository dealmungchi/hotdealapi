package kr.co.dealmungchi.hotdealapi.config.swagger;

import kr.co.dealmungchi.hotdealapi.dto.DealCommentDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for generating example responses for Swagger documentation.
 */
public class SwaggerExampleGenerator {

    /**
     * Generates an example HotDealDto object for API documentation
     */
    public static HotDealDto generateHotDealExample() {
        return HotDealDto.builder()
                .id(1L)
                .title("무선 이어폰 특가")
                .link("https://example.com/deals/123")
                .thumbnailLink("https://example.com/images/123.jpg")
                .price("49,900원")
                .providerId(1L)
                .viewCount(1234L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Generates an example list of HotDealDto objects for API documentation
     */
    public static List<HotDealDto> generateHotDealListExample() {
        return Arrays.asList(
                generateHotDealExample(),
                HotDealDto.builder()
                        .id(2L)
                        .title("노트북 파격 할인")
                        .link("https://example.com/deals/456")
                        .thumbnailLink("https://example.com/images/456.jpg")
                        .price("899,000원")
                        .providerId(2L)
                        .viewCount(5678L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }
    
    /**
     * Generates an example HotDealListResponse object for API documentation
     */
    public static HotDealListResponse generateHotDealListResponseExample() {
        List<HotDealDto> content = generateHotDealListExample();
        return HotDealListResponse.of(content, false, 20);
    }

    /**
     * Generates an example DealCommentDto object for API documentation
     */
    public static DealCommentDto generateDealCommentExample() {
        return DealCommentDto.builder()
                .id(1L)
                .hotdealsId(1L)
                .parentId(null)
                .userId(101L)
                .content("이 제품 강추합니다! 배송도 빠르고 좋네요.")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Generates an example list of DealCommentDto objects for API documentation
     */
    public static List<DealCommentDto> generateDealCommentListExample() {
        DealCommentDto parentComment = generateDealCommentExample();
        
        DealCommentDto childComment = DealCommentDto.builder()
                .id(2L)
                .hotdealsId(1L)
                .parentId(1L)
                .userId(102L)
                .content("저도 구매했는데 만족스럽습니다!")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return Arrays.asList(parentComment, childComment);
    }
}