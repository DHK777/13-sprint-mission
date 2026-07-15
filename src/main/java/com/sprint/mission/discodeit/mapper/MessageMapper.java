package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageDto toDto(Message entity) {
    if (entity == null) {
      return null;
    }
    return new MessageDto(
        entity.getId(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getContent(),
        entity.getChannel().getId(),
        userMapper.toDto(entity.getAuthor()),
        entity.getAttachments().stream().map(binaryContentMapper::toDto).toList()
    );
  }
}