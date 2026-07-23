package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public ChannelDto createPublic(String name, String description) {
    Channel channel = new Channel(name, ChannelType.PUBLIC, description);
    channelRepository.save(channel);
    return convertToDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto createPrivate(List<UUID> participantIds) {
    Channel channel = new Channel(null, ChannelType.PRIVATE, null);
    channelRepository.save(channel);

    for (UUID userId : participantIds) {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
      ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
      readStatusRepository.save(readStatus);
    }
    return convertToDto(channel);
  }

  @Override
  public ChannelDto find(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
    return convertToDto(channel);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(this::convertToDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, String name, String description) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
    channel.update(name, channel.getType(), description);
    return convertToDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    messageRepository.deleteByChannelId(id);
    readStatusRepository.deleteByChannelId(id);

    channelRepository.delete(channel);
  }

  private ChannelDto convertToDto(Channel channel) {
    Slice<Message> latestMessageSlice = messageRepository.findByChannelIdOrderByCreatedAtDesc(
        channel.getId(), PageRequest.of(0, 1)
    );
    Instant lastMessageAt = latestMessageSlice.hasContent() ?
        latestMessageSlice.getContent().get(0).getCreatedAt() : null;

    List<UserDto> participants = readStatusRepository.findByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();

    return channelMapper.toDto(channel, lastMessageAt, participants);
  }
}