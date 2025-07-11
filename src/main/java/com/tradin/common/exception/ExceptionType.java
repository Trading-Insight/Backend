package com.tradin.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {
    //400 Bad Request

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
    INVALID_JWT_SIGNATURE_EXCEPTION(UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN_EXCEPTION(UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN_EXCEPTION(UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    NOT_FOUND_JWT_CLAIMS_EXCEPTION(UNAUTHORIZED, "JWT Claims가 존재하지 않습니다."),

    //403 Forbidden

    //404 Not Found
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_STRATEGY_EXCEPTION(NOT_FOUND, "존재하지 않는 전략입니다."),
    NOT_FOUND_OPEN_POSITION_EXCEPTION(NOT_FOUND, "해당 전략에 대해 오픈된 포지션이 없습니다."),
    NOT_FOUND_ANY_STRATEGY_EXCEPTION(NOT_FOUND, "전략이 아무것도 존재하지 않습니다."),
    NOT_FOUND_SECURITY_CONTEXT_EXCEPTION(NOT_FOUND, "Security Context에 유저 정보가 존재하지 않습니다."),
    NOT_FOUND_ACCOUNT_EXCEPTION(NOT_FOUND, "존재하지 않는 계좌입니다."),
    NOT_FOUND_ANY_ACCOUNT_EXCEPTION(NOT_FOUND, "활성 계좌가 존재하지 않습니다."),
    NOT_SUBSCRIBED_STRATEGY_EXCEPTION(BAD_REQUEST, "구독하지 않은 전략입니다."),
    CANNOT_SUBSCRIBE_SAME_COIN_TYPE_EXCEPTION(BAD_REQUEST, "코인 타입 하나당 하나의 전략만 구독 가능합니다."),
    NOT_FOUND_HISTORY_EXCEPTION(NOT_FOUND, "존재하지 않는 전략 내역입니다."),
    NOT_FOUND_PRICE_EXCEPTION(NOT_FOUND, "시세가 존재하지 않습니다."),
    NOT_FOUND_BALANCE_EXCEPTION(NOT_FOUND, "잔고가 존재하지 않습니다."),
    NOT_FOUND_SUCH_METHOD_EXCEPTION(NOT_FOUND, "존재하지 않는 메소드입니다."),

    //405 Method Not Allowed
    LOCK_ACQUISITION_FAILED_EXCEPTION(BAD_REQUEST, "락을 획득하지 못했습니다."),

    //409 Conflict
    ALREADY_SUBSCRIBED_EXCEPTION(BAD_REQUEST, "이미 구독중인 전략입니다."),
    ALREADY_POSITION_EXIST_EXCEPTION(BAD_REQUEST, "포지션이 존재하는 경우 구독이 불가합니다."),
    SAME_POSITION_REQUEST_EXCEPTION(BAD_REQUEST, "동일한 포지션 요청입니다."),

    //429 Too Many Requests
    IP_RATE_LIMIT_EXCEEDED_EXCEPTION(TOO_MANY_REQUESTS, "IP당 최대 요청 횟수를 초과하였습니다."),

    //500 Internal Server Error
    ENCRYPT_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "암호화에 실패하였습니다."),
    DECRYPT_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "복호화에 실패하였습니다."),
    SIGNATURE_GENERATION_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "JWT 서명 생성에 실패하였습니다."),
    PUBLIC_KEY_GENERATE_FAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "공개키 생성에 실패하였습니다."),
    INTERNAL_SERVER_ERROR_EXCEPTION(INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");;

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionType(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
