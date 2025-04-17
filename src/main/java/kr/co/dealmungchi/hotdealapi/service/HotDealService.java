package kr.co.dealmungchi.hotdealapi.service;

import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.entity.HotDeal;
import kr.co.dealmungchi.hotdealapi.domain.service.HotDealDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotDealService {
    private final HotDealDomainService hotDealDomainService;
    
    public Flux<HotDeal> getHotDealsWithPagination(int page, int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return Flux.error(new BusinessException(ErrorCode.INVALID_REQUEST, 
                        "페이지는 0 이상, 사이즈는 1-100 사이여야 합니다."));
            }
            
            return hotDealDomainService.findHotDealsWithPagination(page, size);
        } catch (Exception e) {
            log.error("Failed to get hot deals with pagination: {}", e.getMessage(), e);
            return Flux.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
    
    public Mono<HotDeal> getHotDealById(Long id) {
        try {
            if (id == null || id <= 0) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 ID입니다."));
            }
            
            return hotDealDomainService.findHotDealById(id);
        } catch (Exception e) {
            log.error("Failed to get hot deal by id: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
    
    public Mono<HotDeal> incrementViewCount(Long id) {
        try {
            if (id == null || id <= 0) {
                return Mono.error(new BusinessException(ErrorCode.INVALID_REQUEST, "유효하지 않은 ID입니다."));
            }
            
            return hotDealDomainService.incrementViewCount(id);
        } catch (Exception e) {
            log.error("Failed to increment view count: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}