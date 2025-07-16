package com.min.ringleaitutorserver.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    // PG사 결제 처리
    public PaymentResult processPayment(String paymentMethod, String paymentToken, Integer amount) {
        if ("invalid_token".equals(paymentToken)) {
            return PaymentResult.failure("유효하지 않은 결제 토큰입니다");
        }
        
        if (amount <= 0) {
            return PaymentResult.failure("유효하지 않은 결제 금액입니다");
        }
        
        simulateProcessingDelay();
        
        String paymentId = UUID.randomUUID().toString();
        return PaymentResult.success(paymentId);
    }
    
    // 결제 처리 지연 시뮬레이션
    private void simulateProcessingDelay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static class PaymentResult {
        private final boolean success;
        private final String paymentId;
        private final String errorMessage;

        private PaymentResult(boolean success, String paymentId, String errorMessage) {
            this.success = success;
            this.paymentId = paymentId;
            this.errorMessage = errorMessage;
        }

        // 결제 성공 결과 생성
        public static PaymentResult success(String paymentId) {
            return new PaymentResult(true, paymentId, null);
        }

        // 결제 실패 결과 생성
        public static PaymentResult failure(String errorMessage) {
            return new PaymentResult(false, null, errorMessage);
        }

        // 결제 성공 여부 확인
        public boolean isSuccess() {
            return success;
        }

        // 결제 ID 조회
        public String getPaymentId() {
            return paymentId;
        }

        // 오류 메시지 조회
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}