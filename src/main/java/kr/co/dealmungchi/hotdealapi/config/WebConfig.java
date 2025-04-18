package kr.co.dealmungchi.hotdealapi.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
		String[] origins = allowedOrigins != null ? Arrays.stream(allowedOrigins.split(","))
				.map(String::trim)
				.toArray(String[]::new)
				: new String[] { "http://localhost:3000" }; // 기본값 설정

		registry.addMapping("/**")
				.allowedOrigins(origins)
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}

	@Override
	public void configurePathMatching(PathMatchConfigurer configurer) {
		configurer.setUseCaseSensitiveMatch(false);
	}

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB

		// JSON 응답 기본 설정
		configurer.defaultCodecs().enableLoggingRequestDetails(true);
	}
}