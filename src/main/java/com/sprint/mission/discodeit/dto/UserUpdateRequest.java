package com.sprint.mission.discodeit.dto;

public record UserUpdateRequest(
    String newEmail,
    String newUsername,
    String newPassword,
    String statusMessage
) {

}
