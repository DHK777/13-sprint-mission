package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private final UUID channelId; // 메시지가 작성된 채널의 ID (작성 후 변경 불가)
    private final UUID authorId;  // 메시지를 작성한 유저의 ID (작성 후 변경 불가)
    private String content;       // 메시지 내용

    public Message(UUID channelId, UUID authorId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;

        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
