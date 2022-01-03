package kr.todoit.exception;

import lombok.Getter;

@Getter
public enum DefaultExceptionType implements ExceptionType {
    NOT_FOUND_USER(1001, 403, "해당하는 사용자가 존재하지 않습니다."),
    DUPLICATED_USER(1002, 403, "이미 존재하는 사용자 아이디입니다."),
    LOGIN_INFO_NOT_FOUND(1003, 403, "로그인 정보를 찾을 수 없습니다.(세션 만료)"),
    NOT_MATCHED_OAUTH_TOKEN(1004, 403, "Oauth 토큰 인증에 실패하였습니다."),
    LOGIN_FAILS(1005, 403, "로그인에 실패하였습니다."),
    EXPIRED_TOKEN(1006, 403, "토큰이 만료되었습니다."),
    NOT_MATCHED_TOKEN(1007, 403, "토큰이 일치하지 않습니다."),
    PERMISSION_NOT_DEFINE(1008, 403, "올바르지 않은 토큰입니다."),
    AUTHENTICATE_NOT_MATCH(1009, 403, "허용되지 않는 접근입니다.");

    private int errorCode;
    private int httpStatus;
    private String errorMessage;

    DefaultExceptionType(int errorCode, int httpStatus, String errorMessage){
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
