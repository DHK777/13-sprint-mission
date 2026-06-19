package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final String FILE_PATH_STR = "binary_contents.dat";
    private final Map<UUID, BinaryContent> store;

    public FileBinaryContentRepository() {
        this.store = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, BinaryContent> loadData() {
        Path filePath = Paths.get(FILE_PATH_STR);
        if (!Files.exists(filePath)) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, BinaryContent> data) {
        Path filePath = Paths.get(FILE_PATH_STR);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("첨부파일 저장 중 오류 발생", e);
        }
    }

    @Override
    public void save(BinaryContent binaryContent) {
        store.put(binaryContent.getId(), binaryContent);
        saveData(store);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
        saveData(store);
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