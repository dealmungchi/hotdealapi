package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.util.Arrays;
import java.util.List;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	private final Environment environment;

	public SwaggerConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Hot Deal API")
						.description("Hot Deal API Documentation")
						.version("v1.0"))
				.servers(getServers());
	}

	private List<Server> getServers() {
		Server productionServer = new Server()
				.url("https://api.dealmungchi.co.kr")
				.description("Production Server");

		// Check if the active profile is "dev"
		if (isDevProfile()) {
			Server devServer = new Server()
					.url("http://localhost:" + getServerPort())
					.description("Development Server");
			return List.of(devServer, productionServer);
		}

		return List.of(productionServer);
	}

	private boolean isDevProfile() {
		return environment.getActiveProfiles().length > 0 &&
				Arrays.asList(environment.getActiveProfiles()).contains("dev");
	}

	private String getServerPort() {
		return environment.getProperty("server.port", "9090");
	}

	@Bean
	public OperationCustomizer apiResponseCustomizer() {
		return new ApiResponseCustomizer();
	}
}