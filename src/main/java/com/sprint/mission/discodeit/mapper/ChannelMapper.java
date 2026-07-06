package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel entity) {
    if (entity == null) {
      return null;
    }

    Instant lastMessageAt = messageRepository.findByChannelId(entity.getId()).stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);

    List<UserDto> participants = readStatusRepository.findByChannelId(entity.getId()).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();

    return new ChannelDto(
        entity.getId(),
        entity.getType(),
        entity.getName(),
        entity.getDescription(),
        participants,
        lastMessageAt
    );
  }
}