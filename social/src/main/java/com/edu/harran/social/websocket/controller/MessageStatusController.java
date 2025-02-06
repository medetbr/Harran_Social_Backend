package com.edu.harran.social.websocket.controller;

import com.edu.harran.social.dto.messageDto.MessageStatusDto;
import com.edu.harran.social.websocket.service.MessageStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/v1/api/messages/status")
public class MessageStatusController {
    private final MessageStatusService messageStatusService;

    @GetMapping("/{messageId}")
    public List<MessageStatusDto> getMessageStatusByMessageId(@PathVariable String messageId, @RequestHeader("Authorization") String token){
    return messageStatusService.findByMessageId(messageId);
    }
}
