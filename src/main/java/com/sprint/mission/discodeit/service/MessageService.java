package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FileUploadDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(UUID channelId, UUID authorId, String content, List<FileUploadDto> files);

  MessageDto read(UUID id);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable);

  MessageDto update(UUID id, String newContent);

  void delete(UUID id);
}