package com.example.demo.service;

import com.example.demo.model.Channel;
import com.example.demo.model.ChannelUser;
import com.example.demo.repository.ChannelRepository;
import com.example.demo.repository.ChannelUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Channel createChannelAndUser(Channel channel, Long userId) {
        // 채널 생성
        Channel savedChannel = channelRepository.save(channel);

        // 채널 사용자 생성
        ChannelUser channelUser = new ChannelUser(savedChannel.getId(), userId);
        channelUserRepository.save(channelUser);

        return savedChannel;
    }

    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
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
