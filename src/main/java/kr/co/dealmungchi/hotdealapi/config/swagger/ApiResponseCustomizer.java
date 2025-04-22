package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;

public class ApiResponseCustomizer implements OperationCustomizer {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		ApiResponseSpec spec = handlerMethod.getMethodAnnotation(ApiResponseSpec.class);
		if (spec == null) {
			throw new IllegalStateException("Missing @ApiResponseSpec on method: " + handlerMethod.getMethod().getName());
		}

		ApiResponses responses = operation.getResponses();
		if (responses == null) {
			responses = new ApiResponses();
			operation.setResponses(responses);
		}

		for (ErrorCode code : spec.errorCodes()) {
			responses.addApiResponse(
					String.valueOf(code.getStatus()),
					buildErrorResponse(code));
		}

		return operation;
	}

	private ApiResponse buildErrorResponse(ErrorCode errorCode) {
		Schema<?> schema = buildSchema(Object.class);
		schema.setExample(exampleFor(errorCode));

		return new ApiResponse()
				.description(errorCode.getMessage())
				.content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
						new MediaType().schema(schema)));
	}

	private Map<String, Object> exampleFor(ErrorCode errorCode) {
		Map<String, Object> error = Map.of(
				"status", errorCode.getStatus(),
				"code", errorCode.getCode(),
				"message", errorCode.getMessage());

		Map<String, Object> response = new HashMap<>();
		response.put("data", null);
		response.put("error", error);
		response.put("localDateTime", LocalDateTime.now().format(FORMATTER));

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Schema<?> buildSchema(Class<?> clazz) {
		Schema<?> schema = new Schema<>().type("object");
		Map<String, Schema<?>> props = Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> !f.isSynthetic())
				.collect(Collectors.toMap(
						Field::getName,
						f -> buildSchema(f.getType())));

		schema.setProperties((Map) props);
		return schema;
	}
}
