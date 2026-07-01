package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(String fileName, String fileUrl, Long size) {
    BinaryContent content = new BinaryContent(
        fileName,
        fileUrl,
        size
    );
    binaryContentRepository.save(content);
    return content;
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 첨부파일을 찾을 수 없습니다."));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids);
  }

  @Override
  public void delete(UUID id) {
    find(id);
    binaryContentRepository.deleteById(id);
  }
}