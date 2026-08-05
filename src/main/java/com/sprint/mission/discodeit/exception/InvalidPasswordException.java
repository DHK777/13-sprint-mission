package com.sprint.mission.discodeit.exception;

public class InvalidPasswordException extends UserException {

  public InvalidPasswordException() {
    super(ErrorCode.INVALID_PASSWORD);
  }
}