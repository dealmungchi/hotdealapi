package kr.co.dealmungchi.hotdealapi.config;

import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomServerCodecConfigurer {

    @Bean
    public CodecCustomizer codecCustomizer() {
        return configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
    }
}