package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class CreateChannelRequest {

    @NotNull
    private Long userId;  // 요청 사용자 ID

    @NotNull
    private Long targetUserId;  // 대상 사용자 ID

    // 기본 생성자
    public CreateChannelRequest() {}

    // 생성자
    public CreateChannelRequest(Long userId, Long targetUserId) {
        this.userId = userId;
        this.targetUserId = targetUserId;
    }

    // Getter와 Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
}
