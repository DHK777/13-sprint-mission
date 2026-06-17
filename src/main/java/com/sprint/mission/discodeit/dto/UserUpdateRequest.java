package com.sprint.mission.discodeit.dto;

public record UserUpdateRequest(
        String email,
        String username,
        String password,
        String statusMessage,

        String profileFileName,
        String profileFileUrl,
        Long profileFileSize
) {}
