package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class ReadStatusNotFoundException extends ChannelException {

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, details);
  }
}