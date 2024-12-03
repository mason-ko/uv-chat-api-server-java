package com.example.demo.controller;

import com.example.demo.dto.CreateMessageRequest;
import com.example.demo.model.Message;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 모든 메시지 조회
    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // ID로 메시지 조회
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> message = messageService.getMessageById(id);
        return message.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 메시지 생성
    @PostMapping
    public Message createMessage(@RequestBody CreateMessageRequest message) {
        return messageService.createMessage(message);
    }

    // 메시지 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody Message newMessage) {
        try {
            Message updatedMessage = messageService.updateMessage(id, newMessage);
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 메시지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
