package kr.co.dealmungchi.hotdealapi.common.exception;

public class BusinessException extends BaseException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}