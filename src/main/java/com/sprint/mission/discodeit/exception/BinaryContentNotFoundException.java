package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(Map<String, Object> details) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
  }
}