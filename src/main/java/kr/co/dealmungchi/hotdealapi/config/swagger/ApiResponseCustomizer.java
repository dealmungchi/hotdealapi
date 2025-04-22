package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;

/**
 * API 응답을 Swagger에 맞게 커스터마이징하는 클래스
 */
public class ApiResponseCustomizer implements OperationCustomizer {

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiResponses responses = Optional.ofNullable(operation.getResponses())
                .orElseGet(() -> {
                    ApiResponses newResponses = new ApiResponses();
                    operation.setResponses(newResponses);
                    return newResponses;
                });

        ApiResponseSpec apiResponseSpec = handlerMethod.getMethodAnnotation(ApiResponseSpec.class);
        if (apiResponseSpec == null) {
            throw new RuntimeException("ApiResponseSpec annotation is required for method " + handlerMethod.getMethod().getName());
        }

        // 성공 응답 추가
        Optional.of(apiResponseSpec.responseClass())
                .filter(clazz -> !clazz.equals(Void.class))
                .ifPresent(clazz -> addSuccessResponse(responses, clazz, handlerMethod.getMethod()));

        // 에러 응답 추가
        Arrays.stream(apiResponseSpec.errorCodes())
                .forEach(errorCode -> addErrorResponse(responses, errorCode));

        return operation;
    }

    private void addSuccessResponse(ApiResponses responses, Class<?> responseClass, Method method) {
        if (List.class.equals(responseClass)) {
            extractListItemTypeFromMethod(method)
                .ifPresentOrElse(
                    itemClass -> generateListResponse(responses, itemClass),
                    () -> addSuccessResponse(responses, (Type) List.class)
                );
        } else {
            addSuccessResponse(responses, (Type) responseClass);
        }
    }
    
    /**
     * 메서드의 반환 타입에서 리스트 항목 타입을 추출
     */
    private Optional<Class<?>> extractListItemTypeFromMethod(Method method) {
        try {
            Type genericReturnType = method.getGenericReturnType();
            if (!(genericReturnType instanceof ParameterizedType)) {
                return Optional.empty();
            }
            
            ParameterizedType outerType = (ParameterizedType) genericReturnType;
            Type[] outerArgs = outerType.getActualTypeArguments();
            if (outerArgs.length == 0 || !(outerArgs[0] instanceof ParameterizedType)) {
                return Optional.empty();
            }
            
            ParameterizedType apiResponseType = (ParameterizedType) outerArgs[0];
            Type[] apiResponseArgs = apiResponseType.getActualTypeArguments();
            if (apiResponseArgs.length == 0 || !(apiResponseArgs[0] instanceof ParameterizedType)) {
                return Optional.empty();
            }
            
            ParameterizedType listType = (ParameterizedType) apiResponseArgs[0];
            if (!List.class.equals(listType.getRawType())) {
                return Optional.empty();
            }
            
            Type[] listArgs = listType.getActualTypeArguments();
            if (listArgs.length == 0 || !(listArgs[0] instanceof Class)) {
                return Optional.empty();
            }
            
            return Optional.of((Class<?>) listArgs[0]);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * 특정 타입의 리스트에 대한 응답 생성
     */
    private void generateListResponse(ApiResponses responses, Class<?> itemClass) {
        Schema<?> schema = SwaggerExampleGenerator.createSchema(List.class);
        schema.setExample(SwaggerExampleGenerator.generateSuccessListApiResponseExample(itemClass));
        responses.addApiResponse("200", createApiResponse(schema));
    }

    private void addSuccessResponse(ApiResponses responses, Type type) {
        Schema<?> schema = SwaggerExampleGenerator.createSchema(type);
        schema.setExample(SwaggerExampleGenerator.generateSuccessApiResponseExample(type));
        responses.addApiResponse("200", createApiResponse(schema));
    }

    private void addErrorResponse(ApiResponses responses, ErrorCode errorCode) {
        Schema<?> schema = SwaggerExampleGenerator.createSchema(Object.class);
        schema.setExample(SwaggerExampleGenerator.generateErrorApiResponseExample(errorCode));
        responses.addApiResponse(String.valueOf(errorCode.getStatus()), createApiResponse(schema, errorCode.getMessage()));
    }
    
    private ApiResponse createApiResponse(Schema<?> schema) {
        return createApiResponse(schema, "Success");
    }
    
    private ApiResponse createApiResponse(Schema<?> schema, String description) {
        return new ApiResponse()
            .description(description)
            .content(new Content().addMediaType(APPLICATION_JSON, new MediaType().schema(schema)));
    }
}