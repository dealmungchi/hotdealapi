package kr.co.dealmungchi.hotdealapi.controller;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.service.HotDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/hotdeals")
@RequiredArgsConstructor
public class HotDealController {
    private final HotDealService hotDealService;
    
    @GetMapping
    public Mono<ApiResponse<List<HotDeal>>> getHotDeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return hotDealService.getHotDealsWithPagination(page, size)
                .collectList()
                .map(ApiResponse::success);
    }
    
    @GetMapping("/{id}")
    public Mono<ApiResponse<HotDeal>> getHotDealById(@PathVariable Long id) {
        return hotDealService.getHotDealById(id)
                .map(ApiResponse::success);
    }
    
    @PatchMapping("/{id}/view-count")
    public Mono<ApiResponse<HotDeal>> incrementViewCount(@PathVariable Long id) {
        return hotDealService.incrementViewCount(id)
                .map(ApiResponse::success);
    }
}