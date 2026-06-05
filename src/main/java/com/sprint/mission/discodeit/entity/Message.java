package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
