package com.sprint.mission.discodeit.dto;

public record UserCreateRequest(
        String email,
        String username,
        String password,
        String profileFileName,
        String profileFileUrl,
        Long profileFileSize
) {}