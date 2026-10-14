package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.JwtDto;
import com.sprint.mission.discodeit.dto.JwtRefreshResult;
import com.sprint.mission.discodeit.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRefreshService {

  private final JwtTokenProvider jwtTokenProvider;
  private final DiscodeitUserDetailsService userDetailsService;

  public JwtRefreshResult refresh(String refreshToken) {
    if (refreshToken == null || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
      throw new InvalidRefreshTokenException();
    }

    try {
      String username = jwtTokenProvider.getUsername(refreshToken);

      DiscodeitUserDetails userDetails =
          (DiscodeitUserDetails) userDetailsService.loadUserByUsername(username);

      String accessToken = jwtTokenProvider.refreshAccessToken(refreshToken);
      String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

      JwtDto jwtDto = new JwtDto(
          userDetails.getUserDto(),
          accessToken
      );

      return new JwtRefreshResult(jwtDto, newRefreshToken);
    } catch (UsernameNotFoundException e) {
      throw new InvalidRefreshTokenException();
    }
  }
}