package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto createPublic(String name, String description);

  ChannelDto createPrivate(List<UUID> participantIds);

  ChannelDto find(UUID id);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID id, String name, String description);

  void delete(UUID id);
}