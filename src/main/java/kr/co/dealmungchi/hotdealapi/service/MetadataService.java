package kr.co.dealmungchi.hotdealapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    /**
     * 모든 프로바이더 목록을 조회합니다.
     * 
     * @return 프로바이더 목록
     */
    public Mono<List<ProviderDto>> getAllProviders() {
        try {
            return providerRepository.findAll()
                    .map(ProviderDto::fromEntity)
                    .collectList();
        } catch (Exception e) {
            log.error("Failed to get all providers: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 모든 카테고리 목록을 조회합니다.
     * 
     * @return 카테고리 목록
     */
    public Mono<List<CategoryDto>> getAllCategories() {
        try {
            return categoryRepository.findAll()
                    .map(CategoryDto::fromEntity)
                    .collectList();
        } catch (Exception e) {
            log.error("Failed to get all categories: {}", e.getMessage(), e);
            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
} 