package com.min.ringleaitutorserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private String paymentId;
    private String status;
    private Integer amount;
    private String message;
    private UserMembershipResponseDto membership;

    public static PaymentResponseDto success(String paymentId, Integer amount, UserMembershipResponseDto membership) {
        return PaymentResponseDto.builder()
                .paymentId(paymentId)
                .status("SUCCESS")
                .amount(amount)
                .message("Payment completed successfully")
                .membership(membership)
                .build();
    }

    public static PaymentResponseDto failure(String message) {
        return PaymentResponseDto.builder()
                .status("FAILED")
                .message(message)
                .build();
    }
}