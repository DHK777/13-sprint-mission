package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

  @Mapping(source = "entity.id", target = "id")
  @Mapping(source = "entity.type", target = "type")
  @Mapping(source = "entity.name", target = "name")
  @Mapping(source = "entity.description", target = "description")
  @Mapping(source = "participants", target = "participants")
  @Mapping(source = "lastMessageAt", target = "lastMessageAt")
  ChannelDto toDto(Channel entity, Instant lastMessageAt, List<UserDto> participants);
}