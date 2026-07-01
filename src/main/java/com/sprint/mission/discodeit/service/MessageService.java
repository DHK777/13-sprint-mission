package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  Message create(UUID channelId, UUID authorId, String content, List<MultipartFile> attachments);

  Message read(UUID id);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID id, String newContent);

  void delete(UUID id);
}