package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User entity) {
    if (entity == null) {
      return null;
    }
    return new UserDto(
        entity.getId(),
        entity.getUsername(),
        entity.getEmail(),
        binaryContentMapper.toDto(entity.getProfile()),
        false
    );
  }
}