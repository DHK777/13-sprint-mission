package com.sprint.mission.discodeit.dto;

public record JwtRefreshResult(
    JwtDto jwtDto,
    String refreshToken
) {

}