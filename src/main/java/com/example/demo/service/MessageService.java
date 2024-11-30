package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 메시지 전체 조회
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // ID로 메시지 조회
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // 메시지 생성
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    // 메시지 업데이트
    public Message updateMessage(Long id, Message newMessage) {
        return messageRepository.findById(id)
                .map(existingMessage -> {
                    existingMessage.setChannelId(newMessage.getChannelId());
                    existingMessage.setUserId(newMessage.getUserId());
                    existingMessage.setContent(newMessage.getContent());
                    existingMessage.setTranslatedContent(newMessage.getTranslatedContent());
                    return messageRepository.save(existingMessage);
                })
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    // 메시지 삭제
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
