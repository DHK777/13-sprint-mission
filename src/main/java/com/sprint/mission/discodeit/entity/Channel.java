package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private ChannelType type;
    private String description;

    public Channel(String name, ChannelType type, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.name = name;
        this.type = type;
        this.description = description;
    }

    public void update(String name, ChannelType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.updatedAt = Instant.now();
    }
}
