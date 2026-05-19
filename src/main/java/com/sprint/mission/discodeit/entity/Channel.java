package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String name;
    private ChannelType type;
    private String description;

    public Channel(String name, ChannelType type, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;

        this.name = name;
        this.type = type;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public void update(String name, ChannelType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }
}
