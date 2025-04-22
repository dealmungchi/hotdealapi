package kr.co.dealmungchi.hotdealapi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactory;
import io.swagger.v3.oas.models.OpenAPI;
import kr.co.dealmungchi.hotdealapi.common.exception.GlobalRestExceptionHandler;
import kr.co.dealmungchi.hotdealapi.config.swagger.ApiResponseCustomizer;
import kr.co.dealmungchi.hotdealapi.config.swagger.SwaggerExampleGenerator;
import kr.co.dealmungchi.hotdealapi.domain.entity.Provider;
import kr.co.dealmungchi.hotdealapi.domain.repository.HotDealRepository;
import kr.co.dealmungchi.hotdealapi.domain.repository.ProviderRepository;
import kr.co.dealmungchi.hotdealapi.domain.service.DealCommentDomainService;
import kr.co.dealmungchi.hotdealapi.domain.service.HotDealDomainService;

@TestConfiguration
@EnableR2dbcRepositories(basePackages = "kr.co.dealmungchi.hotdealapi.domain.repository")
public class TestConfig {

    @Bean
    @Primary
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema-test.sql")));
        initializer.setDatabasePopulator(populator);
        
        return initializer;
    }
    
    @Bean
    @Primary
    public HotDealDomainService hotDealDomainService(HotDealRepository hotDealRepository, ProviderRepository providerRepository) {
        return new HotDealDomainService(hotDealRepository, providerRepository);
    }
    
    @Bean
    @Primary
    public DealCommentDomainService dealCommentDomainService() {
        return new DealCommentDomainService(null, null);
    }
    
    @Bean
    @Primary
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
        org.springframework.data.r2dbc.dialect.R2dbcDialect dialect = org.springframework.data.r2dbc.dialect.DialectResolver.getDialect(connectionFactory);
        java.util.List<org.springframework.core.convert.converter.Converter<?, ?>> converters = new java.util.ArrayList<>();
        converters.add(new ProviderTypeToStringConverter());
        converters.add(new StringToProviderTypeConverter());
        return org.springframework.data.r2dbc.convert.R2dbcCustomConversions.of(dialect, converters);
    }
    
    @Bean
    @Primary
    public GlobalRestExceptionHandler globalRestExceptionHandler() {
        return new GlobalRestExceptionHandler();
    }
    
    @Bean
    @Primary
    public ApiResponseCustomizer apiResponseCustomizer() {
        return new ApiResponseCustomizer();
    }
    
    @Bean
    @Primary
    public SwaggerExampleGenerator swaggerExampleGenerator() {
        return new SwaggerExampleGenerator();
    }
    
    @Bean
    @Primary
    public OpenAPI openAPI() {
        return new OpenAPI();
    }

    @org.springframework.data.convert.WritingConverter
    static class ProviderTypeToStringConverter implements org.springframework.core.convert.converter.Converter<Provider.ProviderType, String> {
        @Override
        public String convert(Provider.ProviderType source) {
            return source.name();
        }
    }

    @org.springframework.data.convert.ReadingConverter
    static class StringToProviderTypeConverter implements org.springframework.core.convert.converter.Converter<String, Provider.ProviderType> {
        @Override
        public Provider.ProviderType convert(String source) {
            return Provider.ProviderType.valueOf(source);
        }
    }
}