package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final String APPLICATION_JSON = "application/json";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiResponses responses = operation.getResponses();
        if (responses == null) {
            responses = new ApiResponses();
            operation.setResponses(responses);
        }

        ApiResponseSpec apiResponseSpec = handlerMethod.getMethodAnnotation(ApiResponseSpec.class);

        if (apiResponseSpec != null) {
            // Add success response based on annotation
            Class<?> responseClass = apiResponseSpec.responseClass();
            if (!responseClass.equals(Void.class)) {
                addSuccessResponse(responses, responseClass, handlerMethod.getMethod());
            }

            // Add specified error responses
            for (ErrorCode errorCode : apiResponseSpec.errorCodes()) {
                addErrorResponse(responses, errorCode);
            }
        } else {
            throw new RuntimeException("ApiResponseSpec annotation is required for method " + handlerMethod.getMethod().getName());
        }

        return operation;
    }

    private void addSuccessResponse(ApiResponses responses, Class<?> responseClass, Method method) {
        // 여기서 List.class인 경우 처리
        if (List.class.equals(responseClass)) {
            processListResponseClass(responses, method);
            return;
        }
        
        // 일반 클래스 처리
        addSuccessResponse(responses, (Type) responseClass);
    }
    
    /**
     * List.class가 responseClass로 설정된 경우, 메서드의 반환 타입으로부터 실제 리스트 항목 타입을 추출하여 처리
     */
    private void processListResponseClass(ApiResponses responses, Method method) {
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            
            if (typeArguments.length > 0) {
                Type responseType = typeArguments[0];
                if (responseType instanceof ParameterizedType) {
                    ParameterizedType nestedType = (ParameterizedType) responseType;
                    Type[] nestedTypeArgs = nestedType.getActualTypeArguments();
                    
                    if (nestedTypeArgs.length > 0) {
                        Type dataType = nestedTypeArgs[0];
                        if (dataType instanceof ParameterizedType) {
                            ParameterizedType listType = (ParameterizedType) dataType;
                            if (List.class.equals((Class<?>) listType.getRawType()) && listType.getActualTypeArguments().length > 0) {
                                // Mono<ApiResponse<List<ItemType>>>
                                Type itemType = listType.getActualTypeArguments()[0];
                                if (itemType instanceof Class) {
                                    generateListResponse(responses, (Class<?>) itemType);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // 타입 추출이 실패한 경우 기본 처리
        addSuccessResponse(responses, (Type) List.class);
    }
    
    /**
     * 특정 타입의 리스트에 대한 응답 생성
     */
    private void generateListResponse(ApiResponses responses, Class<?> itemClass) {
        Schema<?> itemSchema = createSchemaForClass(itemClass);
        Schema<?> arraySchema = new Schema<>().type("array").items(itemSchema);
        
        Map<String, Schema> properties = new HashMap<>();
        properties.put("data", arraySchema);
        properties.put("error", new Schema<>().type("null"));
        properties.put("localDateTime", new Schema<>().type("string").example("2023-01-01 12:00:00"));

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(properties);
                
        // 예시 생성
        Map<String, Object> response = new HashMap<>();
        List<Object> items = List.of(
            createExampleObject(itemClass),
            createExampleObject(itemClass)
        );
        response.put("data", items);
        response.put("error", null);
        response.put("localDateTime", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        
        schema.setExample(response);

        ApiResponse successResponse = new ApiResponse()
                .description("Success")
                .content(new Content().addMediaType(APPLICATION_JSON, new MediaType().schema(schema)));

        responses.addApiResponse("200", successResponse);
    }

    private void addSuccessResponse(ApiResponses responses, Type type) {
        Schema<?> dataSchema = createSchema(type);
        Map<String, Schema> properties = new HashMap<>();
        properties.put("data", dataSchema);
        properties.put("error", new Schema<>().type("null"));
        properties.put("localDateTime", new Schema<>().type("string").example("2023-01-01 12:00:00"));

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(properties);

        // 직접 예제 응답 추가
        schema.setExample(generateApiResponseExample(type));

        ApiResponse successResponse = new ApiResponse()
                .description("Success")
                .content(new Content().addMediaType(APPLICATION_JSON, new MediaType().schema(schema)));

        responses.addApiResponse("200", successResponse);
    }

    private void addErrorResponse(ApiResponses responses, ErrorCode errorCode) {
        Schema<?> errorSchema = new Schema<>()
                .type("object")
                .properties(Map.of(
                        "status", new Schema<>().type("integer").example(errorCode.getStatus()),
                        "code", new Schema<>().type("string").example(errorCode.getCode()),
                        "message", new Schema<>().type("string").example(errorCode.getMessage())));

        Map<String, Schema> properties = new HashMap<>();
        properties.put("data", new Schema<>().type("null"));
        properties.put("error", errorSchema);
        properties.put("localDateTime", new Schema<>().type("string").example("2023-01-01 12:00:00"));

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(properties);

        // 예제 에러 응답 추가
        schema.setExample(SwaggerExampleGenerator.generateErrorApiResponseExample(errorCode));

        ApiResponse errorResponse = new ApiResponse()
                .description(errorCode.getMessage())
                .content(new Content().addMediaType(APPLICATION_JSON, new MediaType().schema(schema)));

        responses.addApiResponse(String.valueOf(errorCode.getStatus()), errorResponse);
    }

    private Schema<?> createSchema(Type type) {
        if (type instanceof Class) {
            return createSchemaForClass((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (rawType.equals(List.class) && typeArguments.length > 0) {
                Schema<?> arraySchema = new Schema<>().type("array");
                if (typeArguments[0] instanceof Class) {
                    arraySchema.setItems(createSchemaForClass((Class<?>) typeArguments[0]));
                }
                return arraySchema;
            }
        }

        return new Schema<>().type("object");
    }

    private Schema<?> createSchemaForClass(Class<?> clazz) {
        Schema<?> schema = new Schema<>().type("object");

        try {
            Field[] fields = clazz.getDeclaredFields();
            Map<String, Schema> properties = new HashMap<>();

            for (Field field : fields) {
                if (!field.isSynthetic()) {
                    Class<?> type = field.getType();

                    if (List.class.equals(type)) {
                        // 리스트 타입 필드는 특별 처리
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType parameterizedType = (ParameterizedType) genericType;
                            Type[] typeArguments = parameterizedType.getActualTypeArguments();
                            if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                                Schema<?> arraySchema = new Schema<>().type("array");
                                arraySchema.setItems(createSchemaForClass((Class<?>) typeArguments[0]));
                                properties.put(field.getName(), arraySchema);
                                continue;
                            }
                        }
                        // 타입 인자를 알 수 없는 경우 빈 배열 스키마
                        properties.put(field.getName(), new Schema<>().type("array"));
                    } else {
                        // 일반 필드는 타입에 맞게 처리
                        Schema<?> fieldSchema = getSchemaForType(type);
                        properties.put(field.getName(), fieldSchema);
                    }
                }
            }

            schema.setProperties(properties);
        } catch (Exception e) {
            // 실패 시 빈 스키마 반환
        }

        return schema;
    }

    private Schema<?> getSchemaForType(Class<?> type) {
        if (String.class.equals(type)) {
            return new Schema<>().type("string").example("string");
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return new Schema<>().type("integer").format("int64").example(0);
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return new Schema<>().type("integer").format("int32").example(0);
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return new Schema<>().type("boolean").example(false);
        } else if (java.time.LocalDateTime.class.equals(type)) {
            return new Schema<>().type("string").format("date-time").example("2023-01-01T12:00:00");
        } else if (isPrimitiveType(type)) {
            // 기본 타입 처리
            return new Schema<>().type("string").example("primitive");
        } else {
            // 사용자 정의 클래스 (DTO)인 경우 중첩 스키마 생성
            return createSchemaForClass(type);
        }
    }

    /**
     * 기본 타입인지 확인하는 메서드입니다.
     */
    private boolean isPrimitiveType(Class<?> type) {
        return type.isPrimitive() ||
                Number.class.isAssignableFrom(type) ||
                type.equals(Character.class) ||
                type.equals(Void.class);
    }

    /**
     * API 응답 예시를 생성합니다.
     */
    private Map<String, Object> generateApiResponseExample(Type type) {
        Map<String, Object> response = new HashMap<>();

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (rawType.equals(List.class) && typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                List<Object> items = List.of(
                        createExampleObject((Class<?>) typeArguments[0]),
                        createExampleObject((Class<?>) typeArguments[0]));
                response.put("data", items);
            } else {
                response.put("data", new HashMap<>());
            }
        } else if (type instanceof Class) {
            response.put("data", createExampleObject((Class<?>) type));
        } else {
            response.put("data", new HashMap<>());
        }

        response.put("error", null);
        response.put("localDateTime", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        return response;
    }

    /**
     * 클래스의 필드를 기반으로 예시 객체를 생성합니다.
     */
    private Map<String, Object> createExampleObject(Class<?> clazz) {
        Map<String, Object> example = new HashMap<>();
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (!field.isSynthetic()) {
                    String fieldName = field.getName();
                    Class<?> type = field.getType();

                    if (String.class.equals(type)) {
                        example.put(fieldName, "string");
                    } else if (Long.class.equals(type) || long.class.equals(type)) {
                        example.put(fieldName, 0L);
                    } else if (Integer.class.equals(type) || int.class.equals(type)) {
                        example.put(fieldName, 0);
                    } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                        example.put(fieldName, false);
                    } else if (java.time.LocalDateTime.class.equals(type)) {
                        example.put(fieldName, "2023-01-01T12:00:00");
                    } else if (List.class.equals(type)) {
                        // 리스트 타입 필드 처리
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType parameterizedType = (ParameterizedType) genericType;
                            Type[] typeArguments = parameterizedType.getActualTypeArguments();
                            if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                                // 리스트의 요소 타입이 있는 경우 예시 리스트 생성
                                Class<?> itemClass = (Class<?>) typeArguments[0];
                                List<Object> listExample = List.of(
                                        createExampleObject(itemClass),
                                        createExampleObject(itemClass));
                                example.put(fieldName, listExample);
                                continue;
                            }
                        }
                        // 요소 타입을 알 수 없는 경우 빈 리스트
                        example.put(fieldName, List.of());
                    } else if (isPrimitiveType(type)) {
                        // 기본 타입 처리
                        example.put(fieldName, 0);
                    } else {
                        // 사용자 정의 클래스 (DTO)인 경우 중첩 객체 생성
                        example.put(fieldName, createExampleObject(type));
                    }
                }
            }
        } catch (Exception e) {
            // 실패 시 빈 객체 반환
        }

        return example;
    }
}