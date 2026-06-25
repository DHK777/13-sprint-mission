package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  List<ChannelDto> findAllByUserId(UUID userId);

  Channel createPublicChannel(PublicChannelCreateRequest request);

  Channel createPrivateChannel(PrivateChannelCreateRequest request);

  Channel update(UUID id, PublicChannelUpdateRequest request);

  void delete(UUID id);
}