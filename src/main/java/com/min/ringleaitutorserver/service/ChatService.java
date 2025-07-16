package com.min.ringleaitutorserver.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.min.ringleaitutorserver.config.AppConfig;
import com.min.ringleaitutorserver.dto.ChatRequestDto;
import com.min.ringleaitutorserver.dto.ChatResponseDto;
import com.min.ringleaitutorserver.exception.BusinessException;
import com.min.ringleaitutorserver.exception.ChatErrorCode;
import com.min.ringleaitutorserver.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MembershipService membershipService;
    private final AppConfig appConfig;
    private final RestTemplate restTemplate;

    // AI 대화 처리
    public ChatResponseDto processChat(ChatRequestDto request) {
        if (!membershipService.useConversation(request.getUserId())) {
            throw new BusinessException(ChatErrorCode.NO_CONVERSATION_CREDITS);
        }

        String userMessage = extractUserMessage(request);
        validateMessage(userMessage);

        String aiResponse = callGemini(userMessage, appConfig.getGemini().getSystemPrompt());

        return ChatResponseDto.builder()
                .userMessage(userMessage)
                .aiResponse(aiResponse)
                .success(true)
                .build();
    }

    // 사용자 메시지 추출
    private String extractUserMessage(ChatRequestDto request) {
        String userMessage = request.getMessage();
        
        if (StringUtils.isBlank(userMessage) && StringUtils.isNotBlank(request.getAudioBase64())) {
            userMessage = transcribeAudio(request.getAudioBase64());
        }
        
        return userMessage;
    }

    // 메시지 검증
    private void validateMessage(String message) {
        if (StringUtils.isBlank(message)) {
            throw new BusinessException(ChatErrorCode.MESSAGE_REQUIRED);
        }
    }

    // 음성을 텍스트로 변환
    private String transcribeAudio(String audioBase64) {
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] audioBytes = Base64.getDecoder().decode(audioBase64);
            ByteString audioData = ByteString.copyFrom(audioBytes);

            RecognitionConfig config = buildRecognitionConfig();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            return extractTranscriptFromResponse(response);
                    
        } catch (Exception e) {
            throw new BusinessException(ChatErrorCode.SPEECH_RECOGNITION_ERROR);
        }
    }

    // 음성 인식 설정 생성
    private RecognitionConfig buildRecognitionConfig() {
        return RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.valueOf(appConfig.getSpeech().getEncoding()))
                .setSampleRateHertz(appConfig.getSpeech().getSampleRateHertz())
                .setLanguageCode(appConfig.getSpeech().getLanguageCode())
                .build();
    }

    // 음성 인식 결과 추출
    private String extractTranscriptFromResponse(RecognizeResponse response) {
        if (response.getResultsList().isEmpty()) {
            return appConfig.getSpeech().getFallbackMessage();
        }
        
        return response.getResults(0)
                .getAlternatives(0)
                .getTranscript();
    }

    // Gemini API 호출
    private String callGemini(String userMessage, String systemPrompt) {
        String apiKey = appConfig.getGemini().getApiKey();
        validateApiKey(apiKey);

        String url = buildGeminiUrl(apiKey);
        Map<String, Object> requestBody = buildRequestBody(userMessage, systemPrompt);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, 
                new HttpEntity<>(requestBody, buildHeaders()), 
                Map.class
            );
            return parseGeminiResponse(response.getBody());
        } catch (Exception e) {
            throw new BusinessException(ChatErrorCode.GEMINI_API_ERROR);
        }
    }

    // API 키 검증
    private void validateApiKey(String apiKey) {
        if (StringUtils.isBlank(apiKey) || "YOUR_GEMINI_API_KEY_HERE".equals(apiKey)) {
            throw new BusinessException(ChatErrorCode.GEMINI_API_KEY_NOT_CONFIGURED);
        }
    }

    // Gemini URL 생성
    private String buildGeminiUrl(String apiKey) {
        return String.format(
            "https://generativelanguage.googleapis.com/v1/models/%s:generateContent?key=%s",
            appConfig.getGemini().getModel(),
            apiKey
        );
    }

    // 요청 모체 생성
    private Map<String, Object> buildRequestBody(String userMessage, String systemPrompt) {
        return Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", systemPrompt + "\n\nUser: " + userMessage)
                ))
            )
        );
    }

    // 헤더 생성
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    // Gemini 응답 파싱
    private String parseGeminiResponse(Map<String, Object> responseBody) {
        try {
            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        return (String) parts.get(0).get("text");
                    }
                }
            }
        } catch (Exception e) {
            // 파싱 오류 시 로그 및 예외 발생
        }
        throw new BusinessException(ChatErrorCode.GEMINI_API_ERROR);
    }
}