package kr.co.dealmungchi.hotdealapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // General errors
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다"),
    INVALID_REQUEST(400, "잘못된 요청입니다"),
    RESOURCE_NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다"),
    
    // HotDeal related errors
    HOTDEAL_NOT_FOUND(404, "핫딜을 찾을 수 없습니다"),
    
    // Comment related errors
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다"),
    PARENT_COMMENT_NOT_FOUND(404, "부모 댓글을 찾을 수 없습니다"),
    INVALID_COMMENT_REQUEST(400, "잘못된 댓글 요청입니다");

    private final int status;
    private final String message;
}