package kr.co.dealmungchi.hotdealapi.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import kr.co.dealmungchi.hotdealapi.common.exception.BusinessException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import kr.co.dealmungchi.hotdealapi.domain.repository.CategoryRepository;
import kr.co.dealmungchi.hotdealapi.domain.repository.ProviderRepository;
import kr.co.dealmungchi.hotdealapi.dto.CategoryDto;
import kr.co.dealmungchi.hotdealapi.dto.ProviderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 메타데이터 서비스
 * 핫딜 관련 메타데이터(프로바이더, 카테고리)를 제공합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataService {

    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    
    // 인메모리 캐시 정의
    private static final Map<String, CacheData<List<ProviderDto>>> PROVIDER_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, CacheData<List<CategoryDto>>> CATEGORY_CACHE = new ConcurrentHashMap<>();
    private static final String CACHE_KEY = "ALL";
    
    // 캐시 유효 시간 (1시간)
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    
    /**
     * 서비스 초기화 시 캐시 데이터 로드
     */
    @PostConstruct
    public void init() {
        // 애플리케이션 시작 시 캐시 데이터 로드
        refreshProviderCache().subscribe(
            providers -> log.info("Provider cache initialized with {} items", providers.size()),
            error -> log.error("Failed to initialize provider cache: {}", error.getMessage(), error)
        );
        
        refreshCategoryCache().subscribe(
            categories -> log.info("Category cache initialized with {} items", categories.size()),
            error -> log.error("Failed to initialize category cache: {}", error.getMessage(), error)
        );
    }

    /**
     * 모든 프로바이더 목록을 조회합니다.
     * 캐시된 데이터가 있으면 캐시에서 반환, 없으면 DB에서 조회 후 캐시에 저장합니다.
     * 
     * @return 프로바이더 목록
     */
    public Mono<List<ProviderDto>> getAllProviders() {
        try {
            CacheData<List<ProviderDto>> cachedData = PROVIDER_CACHE.get(CACHE_KEY);
            
            if (cachedData != null && !cachedData.isExpired()) {
                log.debug("Provider data returned from cache");
                return Mono.just(cachedData.getData());
            }
            
            return refreshProviderCache();
        } catch (Exception e) {
            log.error("Failed to get all providers: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    private Mono<List<ProviderDto>> refreshProviderCache() {
        log.info("Refreshing provider cache");
        return providerRepository.findAll()
                .map(ProviderDto::fromEntity)
                .collectList()
                .doOnSuccess(providers -> {
                    PROVIDER_CACHE.put(CACHE_KEY, new CacheData<>(providers, Instant.now().plus(CACHE_TTL)));
                    log.info("Provider cache refreshed with {} items", providers.size());
                });
    }

    /**
     * 모든 카테고리 목록을 조회합니다.
     * 캐시된 데이터가 있으면 캐시에서 반환, 없으면 DB에서 조회 후 캐시에 저장합니다.
     * 
     * @return 카테고리 목록
     */
    public Mono<List<CategoryDto>> getAllCategories() {
        try {
            CacheData<List<CategoryDto>> cachedData = CATEGORY_CACHE.get(CACHE_KEY);
            
            if (cachedData != null && !cachedData.isExpired()) {
                log.debug("Category data returned from cache");
                return Mono.just(cachedData.getData());
            }
            
            return refreshCategoryCache();
        } catch (Exception e) {
            log.error("Failed to get all categories: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    private Mono<List<CategoryDto>> refreshCategoryCache() {
        log.info("Refreshing category cache");
        return categoryRepository.findAll()
                .map(CategoryDto::fromEntity)
                .collectList()
                .doOnSuccess(categories -> {
                    CATEGORY_CACHE.put(CACHE_KEY, new CacheData<>(categories, Instant.now().plus(CACHE_TTL)));
                    log.info("Category cache refreshed with {} items", categories.size());
                });
    }
    
    /**
     * 캐시 데이터 관리를 위한 내부 클래스
     */
    private static class CacheData<T> {
        private final T data;
        private final Instant expiry;
        
        public CacheData(T data, Instant expiry) {
            this.data = data;
            this.expiry = expiry;
        }
        
        public T getData() {
            return data;
        }
        
        public boolean isExpired() {
            return Instant.now().isAfter(expiry);
        }
    }
} 