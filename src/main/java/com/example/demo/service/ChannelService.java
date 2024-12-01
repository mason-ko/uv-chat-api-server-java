package com.example.demo.service;

import com.example.demo.dto.CreateChannelRequest;
import com.example.demo.model.Channel;
import com.example.demo.model.ChannelUser;
import com.example.demo.repository.ChannelRepository;
import com.example.demo.repository.ChannelUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelUserRepository channelUserRepository;

    @Autowired
    public ChannelService(ChannelRepository channelRepository, ChannelUserRepository channelUserRepository) {
        this.channelRepository = channelRepository;
        this.channelUserRepository = channelUserRepository;
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Optional<Channel> getChannelById(Long id) {
        return channelRepository.findById(id);
    }

    public Channel createChannel(CreateChannelRequest request) {
        // 동일한 유저 ID인지 검증
        if (request.getUserId().equals(request.getTargetUserId())) {
            throw new IllegalArgumentException("UserID and TargetUserID cannot be the same.");
        }

        // 기존 채널 사용자 조회
        List<ChannelUser> existingChannelUsers = channelUserRepository.findByUserIds(
                Arrays.asList(request.getUserId(), request.getTargetUserId())
        );

        if (!existingChannelUsers.isEmpty()) {
            var existsCh = getChannelById(existingChannelUsers.getFirst().getChannelId());
            if (existsCh.isEmpty()) {
                throw new IllegalArgumentException("not found channel.");
            }
            return existsCh.get();
        }

        // 새로운 채널 생성
        Channel newChannel = new Channel();
        newChannel = channelRepository.save(newChannel);  // 새 채널 저장

        // 채널 사용자 생성 및 저장
        for (Long userId : Arrays.asList(request.getUserId(), request.getTargetUserId())) {
            ChannelUser channelUser = new ChannelUser(newChannel.getId(), userId);
            channelUserRepository.save(channelUser);
        }

        return newChannel;
    }

    public Channel updateChannel(Long id, Channel updatedChannel) {
        return channelRepository.findById(id).map(channel -> {
            channel.setName(updatedChannel.getName());
            channel.setDescription(updatedChannel.getDescription());
            channel.setPrivate(updatedChannel.isPrivate());
            channel.setLastContent(updatedChannel.getLastContent());
            channel.setUpdatedAt(updatedChannel.getUpdatedAt());
            return channelRepository.save(channel);
        }).orElseThrow(() -> new RuntimeException("Channel not found with id: " + id));
    }

    public void deleteChannel(Long id) {
        channelRepository.deleteById(id);
    }
}
