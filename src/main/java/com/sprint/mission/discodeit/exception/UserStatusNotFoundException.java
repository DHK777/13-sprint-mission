package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class UserStatusNotFoundException extends UserException {

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_STATUS_NOT_FOUND, details);
  }
}