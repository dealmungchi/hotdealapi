package kr.co.dealmungchi.hotdealapi.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final ErrorCode errorCode;

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BaseException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public BaseException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}