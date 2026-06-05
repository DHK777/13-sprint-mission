package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 고유 식별자
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private UUID profileId;

    private String email;
    private String username;
    private String password;
    private String statusMessage;

    public User(String email, String username, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.email = email;
        this.username = username;
        this.password = password;
        this.statusMessage = "오프라인";
    }

    public void update(String email, String username, String password, String statusMessage) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.statusMessage = statusMessage;
        this.updatedAt = Instant.now();
    }
}
