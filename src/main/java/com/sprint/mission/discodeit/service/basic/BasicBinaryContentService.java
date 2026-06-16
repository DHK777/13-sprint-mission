package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentResponse;
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
    public BinaryContentResponse create(BinaryContentCreateRequest request) {
        BinaryContent content = new BinaryContent(
                request.fileName(),
                request.fileUrl(),
                request.fileSize()
        );
        binaryContentRepository.save(content);
        return toResponse(content);
    }

    @Override
    public BinaryContentResponse find(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 첨부파일을 찾을 수 없습니다."));
        return toResponse(content);
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        find(id);
        binaryContentRepository.deleteById(id);
    }

    private BinaryContentResponse toResponse(BinaryContent content) {
        return new BinaryContentResponse(
                content.getId(), content.getFileName(),
                content.getFileUrl(), content.getFileSize(), content.getCreatedAt()
        );
    }
}
