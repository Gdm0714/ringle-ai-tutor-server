package com.min.ringleaitutorserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
    private String userMessage;
    private String aiResponse;
    private Boolean success;
    private String error;
}
