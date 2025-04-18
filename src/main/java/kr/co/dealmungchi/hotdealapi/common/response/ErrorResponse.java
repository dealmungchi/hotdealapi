package kr.co.dealmungchi.hotdealapi.common.response;

import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;

public record ErrorResponse(
		int status,
		String code,
		String message) {
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
	}

	public static ErrorResponse of(int status, String code, String message) {
		return new ErrorResponse(status, code, message);
	}
}