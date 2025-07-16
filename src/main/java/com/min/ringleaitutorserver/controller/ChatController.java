package com.min.ringleaitutorserver.controller;

import com.min.ringleaitutorserver.dto.ChatRequestDto;
import com.min.ringleaitutorserver.dto.ChatResponseDto;
import com.min.ringleaitutorserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/start")
    public ResponseEntity<ChatResponseDto> startChat(@RequestBody ChatRequestDto request) {
        ChatResponseDto response = chatService.processChat(request);
        return ResponseEntity.ok(response);
    }

}
