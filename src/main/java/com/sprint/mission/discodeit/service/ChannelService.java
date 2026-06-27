package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPublic(String name, String description);

  Channel createPrivate(List<UUID> participantIds);

  Channel find(UUID id);

  List<Channel> findAllByUserId(UUID userId);

  Channel update(UUID id, String name, String description);

  void delete(UUID id);
}