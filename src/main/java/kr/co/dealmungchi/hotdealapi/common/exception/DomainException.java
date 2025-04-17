package kr.co.dealmungchi.hotdealapi.common.exception;

public class DomainException extends BaseException {
    public DomainException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public DomainException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public DomainException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}