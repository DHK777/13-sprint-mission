package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("findById - 성공: 저장된 채널을 ID로 정상 조회")
  void findById_Success() {
    // Given
    Channel channel = new Channel("general", ChannelType.PUBLIC, "General Chat");
    channelRepository.save(channel);

    // When
    Optional<Channel> foundChannel = channelRepository.findById(channel.getId());

    // Then
    assertThat(foundChannel).isPresent();
    assertThat(foundChannel.get().getName()).isEqualTo("general");
    assertThat(foundChannel.get().getType()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("findById - 실패: 존재하지 않는 ID로 조회 시 빈 Optional 반환")
  void findById_Fail_NotFound() {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    // When
    Optional<Channel> result = channelRepository.findById(nonExistentId);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("findAll - 성공: 저장된 모든 채널 목록을 조회")
  void findAll_Success() {
    // Given
    Channel channel1 = new Channel("general", ChannelType.PUBLIC, "General Chat");
    Channel channel2 = new Channel("random", ChannelType.PRIVATE, "Random Chat");
    channelRepository.saveAll(List.of(channel1, channel2));

    // When
    List<Channel> result = channelRepository.findAll();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(Channel::getName)
        .containsExactlyInAnyOrder("general", "random");
  }

  @Test
  @DisplayName("findAll - 성공(빈 결과): 채널이 없을 때 빈 리스트를 반환")
  void findAll_Empty() {
    // Given (빈 데이터베이스 상태)

    // When
    List<Channel> result = channelRepository.findAll();

    // Then
    assertThat(result).isEmpty();
  }
}