package com.example.demo.service;

import com.example.demo.dto.CreateMessageRequest;
import com.example.demo.model.Message;
import com.example.demo.repository.ChannelUserRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.SocketMessageSender;
import com.example.demo.util.TranslationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelUserRepository channelUserRepository;
    private final UserRepository userRepository;
    private final Jedis jedis;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChannelUserRepository channelUserRepository, UserRepository userRepository, Jedis jedis, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.channelUserRepository = channelUserRepository;
        this.userRepository = userRepository;
        this.jedis = jedis;
        this.objectMapper = objectMapper;
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
    public Message createMessage(CreateMessageRequest request) {
        var diffUserId = channelUserRepository.findUserIdInSameChannelWithDifferentIds(request.getChannelId(), request.getUserId());
        var user = userRepository.findById(diffUserId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("UserID not found.");
        }

        var translatedContent = TranslationUtil.sendTranslateHttp(request.getContent(), user.get().getCountry());
        var dbMsg = new Message(request.getChannelId(), request.getUserId(), request.getContent(), translatedContent);
        dbMsg = messageRepository.save(dbMsg);

        try {
            List<String> channels = new ArrayList<>();
            channels.add("channel:" + request.getChannelId());
            SocketMessageSender.sendSocketMessage(jedis, channels, "create_message", dbMsg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert dbMsg to JSON.");
        }

        return dbMsg;
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
