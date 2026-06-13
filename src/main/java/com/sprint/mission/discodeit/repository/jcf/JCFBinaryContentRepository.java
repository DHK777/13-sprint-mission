package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> store = new HashMap<>();

    @Override
    public void save(BinaryContent binaryContent) {
        store.put(binaryContent.getId(), binaryContent);
    }
    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return store.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }
}