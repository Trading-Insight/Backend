package com.tradin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ExceptionMessage {
    //400 Bad Request
    NOT_SUBSCRIBED_STRATEGY_EXCEPTION(BAD_REQUEST, "구독하지 않은 전략입니다."),

    //401 Unauthorized
    EMPTY_HEADER_EXCEPTION(UNAUTHORIZED, "헤더가 비어있습니다."),
    NOT_FOUND_REFRESH_TOKEN_EXCEPTION(UNAUTHORIZED, "존재하지 않는 리프레시 토큰입니다."),
    DIFFERENT_REFRESH_TOKEN_EXCEPTION(UNAUTHORIZED, "DB의 리프레시 토큰과 일치하지 않습니다."),
    INVALID_BEARER_FORMAT_EXCEPTION(UNAUTHORIZED, "Bearer 토큰의 형식이 올바르지 않습니다."),
    INVALID_JWT_TOKEN_EXCEPTION(UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    NOT_FOUND_JWT_USERID_EXCEPTION(UNAUTHORIZED, "JWT 토큰에 유저 아이디가 존재하지 않습니다."),
    WRONG_PASSWORD_EXCEPTION(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS_EXCEPTION(UNAUTHORIZED, "이미 존재하는 이메일입니다."),
    NOT_FOUND_JWK_PARTS_EXCEPTION(UNAUTHORIZED, "존재하지 않는 kid입니다."),

    //403 Forbidden

    //404 Not Found
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_STRATEGY_EXCEPTION(NOT_FOUND, "존재하지 않는 전략입니다."),
    NOT_FOUND_OPEN_POSITION_EXCEPTION(NOT_FOUND, "해당 전략은 오픈된 포지션이 없습니다."),
    NOT_FOUND_ANY_STRATEGY_EXCEPTION(NOT_FOUND, "전략이 아무것도 존재하지 않습니다."),
    NOT_FOUND_SECURITY_CONTEXT_EXCEPTION(NOT_FOUND, "Security Context에 유저 정보가 존재하지 않습니다."),

    //405 Method Not Allowed

    //409 Conflict

    //429 Too Many Requests
    IP_RATE_LIMIT_EXCEEDED_EXCEPTION(TOO_MANY_REQUESTS, "IP당 최대 요청 횟수를 초과하였습니다."),

    //500 Internal Server Error
    ENCRYPT_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "암호화에 실패하였습니다."),
    DECRYPT_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "복호화에 실패하였습니다."),
    SIGNATURE_GENERATION_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "JWT 서명 생성에 실패하였습니다."),
    PUBLIC_KEY_GENERATE_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "공개키 생성에 실패하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
