package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto create(String fileName, String fileUrl, Long size);

  BinaryContentDto find(UUID id);

  List<BinaryContentDto> findAllByIdIn(List<UUID> ids);

  void delete(UUID id);
}
