package kr.co.dealmungchi.hotdealapi.config.swagger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.media.Schema;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;

/**
 * Swagger 문서화를 위한 예시 객체 및 스키마 생성기
 */
public class SwaggerExampleGenerator {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 에러 API 응답 예시 생성
     */
    public static Map<String, Object> generateErrorApiResponseExample(ErrorCode errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", null);

        Map<String, Object> error = new HashMap<>();
        error.put("status", errorCode.getStatus());
        error.put("code", errorCode.getCode());
        error.put("message", errorCode.getMessage());

        response.put("error", error);
        response.put("localDateTime", LocalDateTime.now().format(DATETIME_FORMATTER));
        return response;
    }

    /**
     * 성공 API 응답 예시 생성
     */
    public static Map<String, Object> generateSuccessApiResponseExample(Type type) {
        Object data = createExampleValue(type);
        return createResponseExample(data);
    }

    /**
     * 리스트 타입의 성공 API 응답 예시 생성
     */
    public static Map<String, Object> generateSuccessListApiResponseExample(Class<?> itemClass) {
        List<Object> exampleItems = new ArrayList<>();
        exampleItems.add(createExampleObject(itemClass));
        exampleItems.add(createExampleObject(itemClass));
        return createResponseExample(exampleItems);
    }

    /**
     * API 응답 예시 기본 구조 생성
     */
    private static Map<String, Object> createResponseExample(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("error", null);
        response.put("localDateTime", LocalDateTime.now().format(DATETIME_FORMATTER));
        return response;
    }

    /**
     * 타입에 따른 예시 값 생성
     */
    public static Object createExampleValue(Type type) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return createExampleFromClass(clazz);
        } else if (type instanceof ParameterizedType) {
            return createExampleFromParameterizedType((ParameterizedType) type);
        } else {
            return new HashMap<>();
        }
    }

    /**
     * 클래스 타입에 따른 예시 값 생성
     */
    private static Object createExampleFromClass(Class<?> clazz) {
        // 자바 기본 타입 및 래퍼 클래스 처리
        if (isJavaBuiltInType(clazz)) {
            return createPrimitiveExample(clazz);
        }

        // 컬렉션 타입 처리
        if (Collection.class.isAssignableFrom(clazz)) {
            return new ArrayList<>();
        }

        // 그 외 일반 클래스
        return createExampleObject(clazz);
    }

    /**
     * 자바 기본 제공 타입인지 확인
     */
    private static boolean isJavaBuiltInType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.equals(clazz) ||
                Character.class.equals(clazz) ||
                LocalDateTime.class.equals(clazz) ||
                java.util.Date.class.equals(clazz) ||
                java.sql.Date.class.equals(clazz) ||
                clazz.equals(Void.class);
    }

    /**
     * 기본 타입에 대한 예시 값 생성
     */
    private static Object createPrimitiveExample(Class<?> clazz) {
        if (String.class.equals(clazz)) {
            return "string";
        } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            return 1;
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            return 1L;
        } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            return true;
        } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
            return 1.0;
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            return 1.0f;
        } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
            return (byte) 1;
        } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
            return (short) 1;
        } else if (Character.class.equals(clazz) || char.class.equals(clazz)) {
            return 'A';
        } else if (LocalDateTime.class.equals(clazz)) {
            return "2023-01-01T12:00:00";
        } else if (java.util.Date.class.equals(clazz) || java.sql.Date.class.equals(clazz)) {
            return "2023-01-01";
        } else {
            return null;
        }
    }

    /**
     * 파라미터화된 타입에 따른 예시 값 생성
     */
    private static Object createExampleFromParameterizedType(ParameterizedType parameterizedType) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        // 리스트, 세트 등의 컬렉션 타입 처리
        if (Collection.class.isAssignableFrom(rawType) && typeArguments.length > 0) {
            List<Object> examples = new ArrayList<>();
            Type itemType = typeArguments[0];

            // 요소가 String 또는 기본 타입인 경우 특별 처리
            if (itemType instanceof Class && isJavaBuiltInType((Class<?>) itemType)) {
                Class<?> itemClass = (Class<?>) itemType;
                examples.add(createPrimitiveExample(itemClass));
                examples.add(createPrimitiveExample(itemClass));
            } else {
                // 각 요소 타입에 대한 예시 생성
                examples.add(createExampleValue(itemType));
                examples.add(createExampleValue(itemType));
            }

            return examples;
        }

        // Map 타입 처리
        if (Map.class.isAssignableFrom(rawType) && typeArguments.length >= 2) {
            Map<Object, Object> exampleMap = new HashMap<>();
            Type keyType = typeArguments[0];
            Type valueType = typeArguments[1];

            // 키가 String인 경우만 처리 (가장 일반적인 케이스)
            if (keyType instanceof Class && String.class.equals(keyType)) {
                exampleMap.put("key1", createExampleValue(valueType));
                exampleMap.put("key2", createExampleValue(valueType));
            }

            return exampleMap;
        }

        return new HashMap<>();
    }

    /**
     * 타입에 따른 스키마 생성 - 재귀적으로 중첩 구조 처리
     */
    public static Schema<?> createSchema(Type type) {
        if (type instanceof Class) {
            return createSchemaForClass((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return createSchemaForParameterizedType((ParameterizedType) type);
        }

        return new Schema<>().type("object");
    }

    /**
     * ParameterizedType에 대한 스키마 생성
     */
    private static Schema<?> createSchemaForParameterizedType(ParameterizedType parameterizedType) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        // 컬렉션 타입 처리
        if (Collection.class.isAssignableFrom(rawType) && typeArguments.length > 0) {
            Schema<?> arraySchema = new Schema<>().type("array");
            // 내부 타입에 대해 재귀적으로 스키마 생성
            arraySchema.setItems(createSchema(typeArguments[0]));
            return arraySchema;
        }

        // Map 타입 처리
        if (Map.class.isAssignableFrom(rawType) && typeArguments.length >= 2) {
            return new Schema<>().type("object");
        }

        return new Schema<>().type("object");
    }

    /**
     * 클래스에 대한 스키마 생성
     */
    public static Schema<?> createSchemaForClass(Class<?> clazz) {
        // 자바 기본 타입 및 래퍼 클래스 처리
        if (isJavaBuiltInType(clazz)) {
            Schema<?> schema = createSchemaForPrimitiveType(clazz);
            schema.setExample(createPrimitiveExample(clazz));
            return schema;
        }

        // 컬렉션 타입 처리
        if (Collection.class.isAssignableFrom(clazz)) {
            return new Schema<>().type("array");
        }

        // 그 외 일반 클래스
        Schema<?> schema = new Schema<>().type("object");

        try {
            Map<String, Schema> properties = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !field.isSynthetic())
                    .collect(Collectors.toMap(
                            Field::getName,
                            SwaggerExampleGenerator::createSchemaForField));

            schema.setProperties(properties);
        } catch (Exception e) {
            // 실패 시 빈 스키마 반환
        }

        return schema;
    }

    /**
     * 기본 타입에 대한 스키마 생성
     */
    private static Schema<?> createSchemaForPrimitiveType(Class<?> clazz) {
        if (String.class.equals(clazz)) {
            return new Schema<>().type("string");
        } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            return new Schema<>().type("integer").format("int32");
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            return new Schema<>().type("integer").format("int64");
        } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            return new Schema<>().type("boolean");
        } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
            return new Schema<>().type("number").format("double");
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            return new Schema<>().type("number").format("float");
        } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
            return new Schema<>().type("integer").format("int8");
        } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
            return new Schema<>().type("integer").format("int16");
        } else if (Character.class.equals(clazz) || char.class.equals(clazz)) {
            return new Schema<>().type("string");
        } else if (LocalDateTime.class.equals(clazz)) {
            return new Schema<>().type("string").format("date-time");
        } else if (java.util.Date.class.equals(clazz) || java.sql.Date.class.equals(clazz)) {
            return new Schema<>().type("string").format("date");
        } else {
            return new Schema<>().type("string");
        }
    }

    /**
     * 필드에 대한 스키마 생성
     */
    public static Schema<?> createSchemaForField(Field field) {
        Class<?> type = field.getType();

        // 컬렉션 타입 필드 처리
        if (Collection.class.isAssignableFrom(type)) {
            return createSchemaForCollectionField(field);
        }

        // Map 타입 필드 처리
        if (Map.class.isAssignableFrom(type)) {
            return new Schema<>().type("object");
        }

        // 일반 필드 처리
        return createSchemaForClass(type);
    }

    /**
     * 컬렉션 필드에 대한 스키마 생성 - 재귀적으로 중첩 구조 처리
     */
    public static Schema<?> createSchemaForCollectionField(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Schema<?> arraySchema = new Schema<>().type("array");
                // 내부 타입에 대해 재귀적으로 스키마 생성
                arraySchema.setItems(createSchema(typeArguments[0]));
                return arraySchema;
            }
        }
        // 타입 인자를 알 수 없는 경우 빈 배열 스키마
        return new Schema<>().type("array");
    }

    /**
     * 클래스의 필드를 기반으로 예시 객체 생성
     */
    public static Map<String, Object> createExampleObject(Class<?> clazz) {
        try {
            return Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !field.isSynthetic())
                    .collect(Collectors.toMap(
                            Field::getName,
                            SwaggerExampleGenerator::createExampleFieldValue,
                            (v1, v2) -> v1,
                            HashMap::new));
        } catch (Exception e) {
            // 실패 시 빈 객체 반환
            return new HashMap<>();
        }
    }

    /**
     * 필드 타입에 따른 예시 값 생성
     */
    public static Object createExampleFieldValue(Field field) {
        Class<?> type = field.getType();

        // 자바 기본 타입 및 래퍼 클래스 처리
        if (isJavaBuiltInType(type)) {
            return createPrimitiveExample(type);
        }

        // 컬렉션 타입 필드 처리
        if (Collection.class.isAssignableFrom(type)) {
            return createExampleCollectionValue(field);
        }

        // Map 타입 필드 처리
        if (Map.class.isAssignableFrom(type)) {
            Map<String, Object> exampleMap = new HashMap<>();
            exampleMap.put("key1", "value1");
            exampleMap.put("key2", "value2");
            return exampleMap;
        }

        // 그 외 일반 클래스
        return createExampleObject(type);
    }

    /**
     * 컬렉션 필드에 대한 예시 값 생성 - 재귀적으로 중첩 구조 처리
     */
    public static Object createExampleCollectionValue(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Type itemType = typeArguments[0];
                List<Object> examples = new ArrayList<>();

                // 요소가 String 또는 기본 타입인 경우 특별 처리
                if (itemType instanceof Class && isJavaBuiltInType((Class<?>) itemType)) {
                    Class<?> itemClass = (Class<?>) itemType;
                    examples.add(createPrimitiveExample(itemClass));
                    examples.add(createPrimitiveExample(itemClass));
                } else {
                    // 각 요소 타입에 대한 예시 생성
                    examples.add(createExampleValue(itemType));
                    examples.add(createExampleValue(itemType));
                }

                return examples;
            }
        }
        // 요소 타입을 알 수 없는 경우 빈 리스트
        return List.of();
    }
}