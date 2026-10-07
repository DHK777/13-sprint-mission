package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MdcSecurityContextTaskDecorator implements TaskDecorator {

  @Override
  @NonNull
  public Runnable decorate(@NonNull Runnable runnable) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap();

    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

    return () -> {
      Map<String, String> previousContextMap = MDC.getCopyOfContextMap();
      SecurityContext previousSecurityContext = SecurityContextHolder.getContext();

      try {
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        } else {
          MDC.clear();
        }

        SecurityContext securityContext =
            SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        runnable.run();

      } finally {
        MDC.clear();

        if (previousContextMap != null) {
          MDC.setContextMap(previousContextMap);
        }

        SecurityContextHolder.setContext(previousSecurityContext);
      }
    };
  }
}