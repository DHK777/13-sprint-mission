package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class NotificationAccessDeniedException extends DiscodeitException {

  public NotificationAccessDeniedException(Map<String, Object> details) {
    super(ErrorCode.NOTIFICATION_ACCESS_DENIED, details);
  }
}