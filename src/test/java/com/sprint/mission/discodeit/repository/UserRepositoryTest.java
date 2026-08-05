package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("existsByUsername - 성공: 존재하는 username 검색 시 true 반환")
  void existsByUsername_Success() {
    // Given
    User user = new User("test@test.com", "tester", "pass");
    userRepository.save(user);

    // When
    boolean exists = userRepository.existsByUsername("tester");

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("existsByUsername - 실패(결과없음): 존재하지 않는 username 검색 시 false 반환")
  void existsByUsername_Fail() {
    // Given (빈 데이터베이스 상태)

    // When
    boolean exists = userRepository.existsByUsername("nonexistent");

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("findByUsername - 성공: EntityGraph가 적용된 상태로 유저 정상 조회")
  void findByUsername_Success() {
    // Given
    User user = new User("test2@test.com", "tester2", "pass");
    userRepository.save(user);

    // When
    Optional<User> foundUser = userRepository.findByUsername("tester2");

    // Then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getEmail()).isEqualTo("test2@test.com");
  }

  @Test
  @DisplayName("findByUsername - 실패(결과없음): 존재하지 않는 유저 조회 시 빈 Optional 반환")
  void findByUsername_Fail() {
    // Given (빈 데이터베이스 상태)

    // When
    Optional<User> foundUser = userRepository.findByUsername("unknown");

    // Then
    assertThat(foundUser).isEmpty();
  }
}