package kr.co.dealmungchi.hotdealapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.dealmungchi.hotdealapi.common.response.ApiResponse;
import kr.co.dealmungchi.hotdealapi.dto.CategoryDto;
import kr.co.dealmungchi.hotdealapi.dto.ProviderDto;
import kr.co.dealmungchi.hotdealapi.service.MetadataService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hotdeals")
@RequiredArgsConstructor
public class MetadataController implements MetadataControllerSpec {
    
    private final MetadataService metadataService;

    @Override
    @GetMapping("/providers")
    public Mono<ApiResponse<List<ProviderDto>>> getAllProviders() {
        return metadataService.getAllProviders()
                .map(ApiResponse::success);
    }

    @Override
    @GetMapping("/categories")
    public Mono<ApiResponse<List<CategoryDto>>> getAllCategories() {
        return metadataService.getAllCategories()
                .map(ApiResponse::success);
    }
} 