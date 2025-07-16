package com.min.ringleaitutorserver.exception;

import org.springframework.http.HttpStatus;

public enum MembershipErrorCode implements ErrorCode {
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "멤버십을 찾을 수 없습니다"),
    USER_MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "사용자 멤버십을 찾을 수 없습니다"),
    MEMBERSHIP_EXPIRED(HttpStatus.BAD_REQUEST, "M003", "멤버십이 만료되었습니다"),
    USAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "M004", "사용 횟수가 초과되었습니다"),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "M005", "결제에 실패했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    MembershipErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}