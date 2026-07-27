package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class ReadStatusAlreadyExistsException extends ChannelException {

  public ReadStatusAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_READ_STATUS, details);
  }
}