package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Hot Deal API")
						.description("Hot Deal API Documentation")
						.version("v1.0"))
				.servers(List.of(
						new Server()
								.url("https://api.dealmungchi.co.kr")
								.description("Production Server")));
	}
}