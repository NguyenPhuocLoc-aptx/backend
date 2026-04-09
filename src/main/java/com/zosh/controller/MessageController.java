package com.zosh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zosh.exception.ChatException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Message;
import com.zosh.request.CreateMessageRequest;
import com.zosh.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest request)
            throws UserException, ChatException, ProjectException {
        // ✅ senderId and projectId are now String
        Message sentMessage = messageService.sendMessage(
                request.getSenderId(), request.getProjectId(), request.getContent());
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(
            @PathVariable String projectId)             // ✅ Long → String
            throws ProjectException, ChatException {
        return ResponseEntity.ok(messageService.getMessagesByProjectId(projectId));
    }
}