package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponse create(MessageCreateRequest request);
    MessageResponse read(UUID id);
    List<MessageResponse> findAllByChannelId(UUID channelId);
    MessageResponse update(UUID id, MessageUpdateRequest request);
    void delete(UUID id);
}