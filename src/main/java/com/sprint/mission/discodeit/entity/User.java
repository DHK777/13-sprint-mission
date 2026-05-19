package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String email;
    private String username;
    private String password;
    private String statusMessage;

    public User(String email, String username, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;

        this.email = email;
        this.username = username;
        this.password = password;
        this.statusMessage = "오프라인";
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

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void update(String username, String password, String statusMessage) {
        this.username = username;
        this.password = password;
        this.statusMessage = statusMessage;
        this.updatedAt = System.currentTimeMillis();
    }
}
