package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class NotificationNotFoundException extends DiscodeitException {

  public NotificationNotFoundException(Map<String, Object> details) {
    super(ErrorCode.NOTIFICATION_NOT_FOUND, details);
  }
}