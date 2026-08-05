package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("findByChannelIdOrderByCreatedAtDesc - 성공: 채널의 메시지를 최신순으로 페이징 조회")
  void findByChannelIdOrderByCreatedAtDesc_Success() throws InterruptedException {
    // Given
    User user = new User("test@test.com", "tester", "pass");
    Channel channel = new Channel("general", ChannelType.PUBLIC, "desc");
    em.persist(user);
    em.persist(channel);

    Message msg1 = new Message(channel, user, "First Message");
    em.persist(msg1);

    Thread.sleep(100);

    Message msg2 = new Message(channel, user, "Second Message");
    em.persist(msg2);
    em.flush();

    // When
    Slice<Message> result = messageRepository.findByChannelIdOrderByCreatedAtDesc(
        channel.getId(), PageRequest.of(0, 10));

    // Then
    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getContent().get(0).getContent()).isEqualTo("Second Message");
  }

  @Test
  @DisplayName("findByChannelIdOrderByCreatedAtDesc - 실패: 메시지가 없는 채널 조회 시 빈 Slice 반환")
  void findByChannelIdOrderByCreatedAtDesc_Fail_Empty() {
    // Given
    UUID emptyChannelId = UUID.randomUUID();

    // When
    Slice<Message> result = messageRepository.findByChannelIdOrderByCreatedAtDesc(
        emptyChannelId, PageRequest.of(0, 10));

    // Then
    assertThat(result.getContent()).isEmpty();
    assertThat(result.hasNext()).isFalse();
  }

  @Test
  @DisplayName("findByChannelIdAndCreatedAtLessThan - 성공: 특정 커서(시간) 이전의 메시지만 조회")
  void cursorPaging_Success() throws InterruptedException {
    // Given
    User user = new User("test2@test.com", "tester2", "pass");
    Channel channel = new Channel("random", ChannelType.PUBLIC, "desc");
    em.persist(user);
    em.persist(channel);

    Message oldMsg = new Message(channel, user, "Old Message");
    em.persist(oldMsg);

    Thread.sleep(100);

    Message newMsg = new Message(channel, user, "New Message");
    em.persist(newMsg);

    em.flush();
    em.clear();

    Message fetchedNewMsg = em.find(Message.class, newMsg.getId());
    Instant dbCursor = fetchedNewMsg.getCreatedAt();

    // When
    Slice<Message> result = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
        channel.getId(), dbCursor, PageRequest.of(0, 10));

    // Then
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getContent()).isEqualTo("Old Message");
  }
}