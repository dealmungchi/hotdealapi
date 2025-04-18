package kr.co.dealmungchi.hotdealapi.config.swagger;

import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponseSpec {
    /**
     * The class type of success response. 
     * For List responses, use ResponseClass.class and explicitly set isResponseArray to true.
     */
    Class<?> responseClass() default Void.class;
    
    /**
     * Possible error codes that this API might return.
     */
    ErrorCode[] errorCodes() default {};
}