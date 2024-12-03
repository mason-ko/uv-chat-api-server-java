package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class CreateMessageRequest {

    @NotNull
    private Long userId;  // 요청 사용자 ID

    @NotNull
    private Long channelId;  // 대상 사용자 ID

    @NotNull
    private String content;  // 대상 사용자 ID

    // 기본 생성자
    public CreateMessageRequest() {}

    public CreateMessageRequest(Long userId, Long channelId, String content) {
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
