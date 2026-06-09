package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    void deleteById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    BinaryContent findById(UUID id);
}