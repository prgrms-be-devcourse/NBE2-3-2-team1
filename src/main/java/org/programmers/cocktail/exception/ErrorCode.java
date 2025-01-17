package org.programmers.cocktail.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {// 예외 발생시, body에 실어 날려줄 형태, code, message 커스텀
    BAD_REQUEST(400, -1001, "유효하지 않은 요청입니다."),
    UNAUTHORIZED(401, -1006, "인증에 실패했습니다"),
    USER_NOT_FOUND(404, -2001, "해당 아이디를 찾을 수 없습니다."),
    NOT_FOUND(404, -1002, "잘못된 경로입니다."),
    METHOD_NOT_ALLOWED(405, -1003,"잘못된 Http Method 입니다."),
    INTERNAL_SERVER_ERROR(500, -1004, "서버 내부 오류입니다."),
    INVALID_SORT_TYPE(400, -1010, "올바르지 않은 정렬 타입입니다."),
    INVALID_PASSWORD(400, -1007, "비밀번호가 올바르지 않습니다."),
    DATABASE_ERROR(500, -3000, "Database operation failed.");



    private final int status;
    private final int code;
    private final String message;

    //1. status = 날려줄 상태 코드
    //2. code = 해당 오류가 어느 부부분과 관련있는지 카테고리화 해주는 코드
    //3. message = 발생한 예외에 대해서 설명
    ErrorCode(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
