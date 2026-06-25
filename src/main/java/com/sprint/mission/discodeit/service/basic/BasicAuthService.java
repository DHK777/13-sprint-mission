package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public User login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

    if (!user.getPassword().equals(request.password())) {
      throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
    return user;
  }
}