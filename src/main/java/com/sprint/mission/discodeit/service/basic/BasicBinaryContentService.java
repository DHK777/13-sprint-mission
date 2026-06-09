package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
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
    public BinaryContent create(BinaryContentCreateRequest request) {
        BinaryContent content = new BinaryContent(
                request.fileName(),
                request.fileUrl(),
                request.fileSize()
        );
        binaryContentRepository.save(content);
        return content;
    }

    @Override
    public BinaryContent find(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id);
        if (content == null) {
            throw new IllegalArgumentException("해당 첨부파일을 찾을 수 없습니다.");
        }
        return content;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        // [요구사항] 창고에 뚫어둔 IN 메서드 호출!
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void delete(UUID id) {
        // 있는지 검사하고 폭파
        find(id);
        binaryContentRepository.deleteById(id);
    }
}
