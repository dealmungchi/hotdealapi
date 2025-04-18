package kr.co.dealmungchi.hotdealapi.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// General errors
	RESOURCE_NOT_FOUND(404, "DEAL_29997", "요청한 리소스를 찾을 수 없습니다"),
	INVALID_REQUEST(400, "DEAL_29998", "잘못된 요청입니다"),
	INTERNAL_SERVER_ERROR(500, "DEAL_29999", "서버 오류가 발생했습니다"),

	// HotDeal related errors
	HOTDEAL_NOT_FOUND(404, "DEAL_10000", "핫딜을 찾을 수 없습니다"),

	// Comment related errors
	COMMENT_NOT_FOUND(404, "DEAL_20000", "댓글을 찾을 수 없습니다"),
	PARENT_COMMENT_NOT_FOUND(404, "DEAL_20001", "부모 댓글을 찾을 수 없습니다"),
	INVALID_COMMENT_REQUEST(400, "DEAL_20002", "잘못된 댓글 요청입니다"),
	;

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}