package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public BinaryContentDto create(String fileName, String fileUrl, Long size) {
    BinaryContent content = new BinaryContent(fileName, fileUrl, size);
    binaryContentRepository.save(content);
    return binaryContentMapper.toDto(content);
  }

  @Override
  public BinaryContentDto find(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 첨부파일을 찾을 수 없습니다."));
    return binaryContentMapper.toDto(content);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllById(ids).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 첨부파일을 찾을 수 없습니다."));
    binaryContentRepository.delete(content);
  }
}