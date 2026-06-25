package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  Message create(MessageCreateRequest request, List<MultipartFile> attachments);

  Message read(UUID id);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID id, MessageUpdateRequest request);

  void delete(UUID id);
}