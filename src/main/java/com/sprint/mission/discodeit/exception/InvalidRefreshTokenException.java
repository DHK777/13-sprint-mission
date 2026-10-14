package com.sprint.mission.discodeit.exception;

public class InvalidRefreshTokenException extends DiscodeitException {

  public InvalidRefreshTokenException() {
    super(ErrorCode.INVALID_REFRESH_TOKEN);
  }
}