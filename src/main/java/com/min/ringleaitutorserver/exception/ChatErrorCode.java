package com.min.ringleaitutorserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    
    NO_CONVERSATION_CREDITS(HttpStatus.BAD_REQUEST, "C001", "대화 가능한 횟수가 없습니다"),
    MESSAGE_REQUIRED(HttpStatus.BAD_REQUEST, "C002", "메시지가 필요합니다"),
    GEMINI_API_KEY_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "Gemini API 키가 설정되지 않았습니다"),
    GEMINI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "AI 응답 생성 중 오류가 발생했습니다"),
    SPEECH_RECOGNITION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "음성 인식 처리 중 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}