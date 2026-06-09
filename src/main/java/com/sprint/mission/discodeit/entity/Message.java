package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private List<UUID> attachmentIds = new ArrayList<>();


    private final UUID channelId; // 메시지가 작성된 채널의 ID (작성 후 변경 불가)
    private final UUID authorId;  // 메시지를 작성한 유저의 ID (작성 후 변경 불가)
    private String content;       // 메시지 내용

    public Message(UUID channelId, UUID authorId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void addAttachmentId(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
    }
}
