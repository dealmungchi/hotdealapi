package kr.co.dealmungchi.hotdealapi.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;

import java.util.ArrayList;
import java.util.List;

import kr.co.dealmungchi.hotdealapi.domain.entity.Provider.ProviderType;

@Configuration
public class R2dbcConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
        R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ProviderTypeToStringConverter());
        converters.add(new StringToProviderTypeConverter());
        return R2dbcCustomConversions.of(dialect, converters);
    }

    @WritingConverter
    static class ProviderTypeToStringConverter implements Converter<ProviderType, String> {
        @Override
        public String convert(ProviderType source) {
            return source.name();
        }
    }

    @ReadingConverter
    static class StringToProviderTypeConverter implements Converter<String, ProviderType> {
        @Override
        public ProviderType convert(String source) {
            return ProviderType.valueOf(source);
        }
    }
}