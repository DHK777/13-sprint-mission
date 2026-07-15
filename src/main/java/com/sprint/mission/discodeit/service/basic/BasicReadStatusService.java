package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(UUID channelId, UUID userId, Instant lastReadAt) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    if (readStatusRepository.findByChannelIdAndUserId(channelId, userId).isPresent()) {
      throw new IllegalArgumentException("해당 유저는 이미 이 채널의 읽음 상태를 가지고 있습니다.");
    }

    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태창을 찾을 수 없습니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId);
  }

  @Override
  public ReadStatus update(UUID id, Instant newLastReadAt) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태창을 찾을 수 없습니다."));
    readStatus.updateLastReadAt();
    return readStatus;
  }

  @Override
  public void delete(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태창을 찾을 수 없습니다."));
    readStatusRepository.delete(readStatus);
  }
}