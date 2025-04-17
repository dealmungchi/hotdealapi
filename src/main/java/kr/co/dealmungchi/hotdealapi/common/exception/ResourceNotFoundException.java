package kr.co.dealmungchi.hotdealapi.common.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
    
    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}