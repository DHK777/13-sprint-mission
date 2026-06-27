package com.sprint.mission.discodeit.service.basic;

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
  public User login(String username, String password) {
    User user = userRepository.findAll().stream()
        .filter(u -> u.getUsername().equals(username))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저명입니다."));

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    return user;
  }
}