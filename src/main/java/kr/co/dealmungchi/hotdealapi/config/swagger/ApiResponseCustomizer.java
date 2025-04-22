package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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
import kr.co.dealmungchi.hotdealapi.dto.DealCommentDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealDto;
import kr.co.dealmungchi.hotdealapi.dto.HotDealListResponse;

public class ApiResponseCustomizer implements OperationCustomizer {

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiResponseSpec apiResponseSpec = handlerMethod.getMethodAnnotation(ApiResponseSpec.class);

        // Add ApiResponseSpec annotation if present
        if (apiResponseSpec != null) {
            customizeWithAnnotation(operation, apiResponseSpec, handlerMethod.getMethod());
            return operation;
        }

        // Default customization based on return type
        return customizeBasedOnReturnType(operation, handlerMethod.getMethod());
    }

    private void customizeWithAnnotation(Operation operation, ApiResponseSpec apiResponseSpec, Method method) {
        ApiResponses responses = operation.getResponses();
        if (responses == null) {
            responses = new ApiResponses();
            operation.setResponses(responses);
        }

        // Add success response
        Class<?> responseClass = apiResponseSpec.responseClass();
        if (!responseClass.equals(Void.class)) {
            addSuccessResponse(responses, responseClass, method);
        }

        // Add error responses
        for (ErrorCode errorCode : apiResponseSpec.errorCodes()) {
            addErrorResponse(responses, errorCode);
        }
    }

    private Operation customizeBasedOnReturnType(Operation operation, Method method) {
        ApiResponses responses = operation.getResponses();
        if (responses == null) {
            responses = new ApiResponses();
            operation.setResponses(responses);
        }

        // Add success response based on return type
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length > 0) {
                Type dataType = typeArguments[0];
                if (dataType instanceof ParameterizedType) {
                    // Handle nested generic types like Mono<ApiResponse<List<HotDealDto>>>
                    ParameterizedType nestedType = (ParameterizedType) dataType;
                    Type[] nestedTypeArgs = nestedType.getActualTypeArguments();
                    if (nestedTypeArgs.length > 0) {
                        addSuccessResponseFromType(responses, nestedTypeArgs[0]);
                    }
                } else {
                    // Handle simple generic types like Mono<ApiResponse<HotDealDto>>
                    addSuccessResponseFromType(responses, dataType);
                }
            }
        }

        // Add common error responses
        final ApiResponses apiResponses = responses;
        Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.getStatus() >= 400)
                .forEach(errorCode -> addErrorResponse(apiResponses, errorCode));

        return operation;
    }

    private void addSuccessResponse(ApiResponses responses, Class<?> responseClass, Method method) {
        Map<String, Schema> properties = new HashMap<>();
        properties.put("data", createSchemaForType(responseClass, method));
        properties.put("error", new Schema<>().nullable(Boolean.TRUE).type("object"));
        properties.put("localDateTime", new Schema<>().type("string").example("2023-01-01 12:00:00"));

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(properties);

        ApiResponse successResponse = new ApiResponse()
                .description("Success")
                .content(new Content().addMediaType(APPLICATION_JSON, new MediaType().schema(schema)));

        responses.addApiResponse("200", successResponse);
    }

    private void addSuccessResponseFromType(ApiResponses responses, Type type) {
        Schema<?> dataSchema;
        try {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                Type[] typeArguments = parameterizedType.getActualTypeArguments();

                if (rawType.equals(java.util.List.class) && typeArguments.length > 0) {
                    Class<?> itemClass = (Class<?>) typeArguments[0];
                    dataSchema = new Schema<>()
                            .type("array")
                            .items(createSchemaForClass(itemClass));

                    // Add example based on item class
                    if (itemClass.equals(HotDealDto.class)) {
                        dataSchema.setExample(SwaggerExampleGenerator.generateHotDealListExample());
                    } else if (itemClass.equals(DealCommentDto.class)) {
                        dataSchema.setExample(SwaggerExampleGenerator.generateDealCommentListExample());
                    }
                } else {
                    dataSchema = new Schema<>().type("object");
                }
            } else if (type instanceof Class) {
                dataSchema = createSchemaForClass((Class<?>) type);

                // Add example based on class
                if (type.equals(HotDealDto.class)) {
                    dataSchema.setExample(SwaggerExampleGenerator.generateHotDealExample());
                } else if (type.equals(DealCommentDto.class)) {
                    dataSchema.setExample(SwaggerExampleGenerator.generateDealCommentExample());
                } else if (type.equals(HotDealListResponse.class)) {
                    dataSchema.setExample(SwaggerExampleGenerator.generateHotDealListResponseExample());
                }
            } else {
                dataSchema = new Schema<>().type("object");
            }
        } catch (Exception e) {
            dataSchema = new Schema<>().type("object");
        }

        Map<String, Schema> properties = new HashMap<>();
        properties.put("data", dataSchema);
        properties.put("error", new Schema<>().nullable(Boolean.TRUE).type("null"));
        properties.put("localDateTime", new Schema<>().type("string").example("2023-01-01 12:00:00"));

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(properties);

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
        properties.put("data", new Schema<>().nullable(Boolean.TRUE).type("null"));
        properties.put("error", errorSchema);
        properties.put("localDateTime", new Schema<>().type("string").example("2023-01-01 12:00:00"));

        Schema<?> schema = new Schema<>()
                .type("object")
                .properties(properties);

        ApiResponse errorResponse = new ApiResponse()
                .description(errorCode.getMessage())
                .content(new Content().addMediaType(APPLICATION_JSON, new MediaType().schema(schema)));

        responses.addApiResponse(String.valueOf(errorCode.getStatus()), errorResponse);
    }

    private Schema<?> createSchemaForType(Class<?> clazz, Method method) {
        // If the class is a List, we need to handle it differently
        if (clazz.equals(List.class)) {
            Schema<?> schema = new Schema<>().type("array");

            // Try to determine the list item type from the method's return type
            Type returnType = method.getGenericReturnType();
            if (returnType instanceof ParameterizedType) {
                // For Mono<ApiResponse<List<T>>>
                ParameterizedType parameterizedType = (ParameterizedType) returnType;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();

                if (typeArguments.length > 0 && typeArguments[0] instanceof ParameterizedType) {
                    ParameterizedType responseType = (ParameterizedType) typeArguments[0];
                    Type[] responseTypeArgs = responseType.getActualTypeArguments();

                    if (responseTypeArgs.length > 0 && responseTypeArgs[0] instanceof ParameterizedType) {
                        ParameterizedType listType = (ParameterizedType) responseTypeArgs[0];
                        Type[] listTypeArgs = listType.getActualTypeArguments();

                        if (listTypeArgs.length > 0 && listTypeArgs[0] instanceof Class) {
                            Class<?> itemClass = (Class<?>) listTypeArgs[0];
                            schema.setItems(createSchemaForClass(itemClass));

                            // Add example based on item class
                            if (itemClass.equals(HotDealDto.class)) {
                                schema.setExample(SwaggerExampleGenerator.generateHotDealListExample());
                            } else if (itemClass.equals(DealCommentDto.class)) {
                                schema.setExample(SwaggerExampleGenerator.generateDealCommentListExample());
                            }
                        }
                    }
                }
            }

            return schema;
        } else {
            Schema<?> schema = createSchemaForClass(clazz);

            // Add example based on class
            if (clazz.equals(HotDealDto.class)) {
                schema.setExample(SwaggerExampleGenerator.generateHotDealExample());
            } else if (clazz.equals(DealCommentDto.class)) {
                schema.setExample(SwaggerExampleGenerator.generateDealCommentExample());
            } else if (clazz.equals(HotDealListResponse.class)) {
                schema.setExample(SwaggerExampleGenerator.generateHotDealListResponseExample());
            }

            return schema;
        }
    }

    private Schema<?> createSchemaForClass(Class<?> clazz) {
        Schema<?> schema = new Schema<>().type("object");

        try {
            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            Map<String, Schema> properties = new HashMap<>();

            for (java.lang.reflect.Field field : fields) {
                if (!field.isSynthetic()) { // Filter out synthetic fields added by compiler for record
                    Schema<?> fieldSchema;
                    Class<?> type = field.getType();

                    if (String.class.equals(type)) {
                        fieldSchema = new Schema<>().type("string");
                    } else if (Long.class.equals(type) || long.class.equals(type)) {
                        fieldSchema = new Schema<>().type("integer").format("int64");
                    } else if (Integer.class.equals(type) || int.class.equals(type)) {
                        fieldSchema = new Schema<>().type("integer").format("int32");
                    } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                        fieldSchema = new Schema<>().type("boolean");
                    } else if (java.time.LocalDateTime.class.equals(type)) {
                        fieldSchema = new Schema<>().type("string").format("date-time");
                    } else {
                        fieldSchema = new Schema<>().type("object");
                    }

                    properties.put(field.getName(), fieldSchema);
                }
            }

            schema.setProperties(properties);
        } catch (Exception e) {
            // If reflection fails, return generic object schema
        }

        return schema;
    }
}