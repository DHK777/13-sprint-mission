package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH_STR = "channels.dat";
    private final Map<UUID, Channel> store;

    public FileChannelRepository() {
        this.store = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        Path filePath = Paths.get(FILE_PATH_STR);
        if (!Files.exists(filePath)) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Channel> data) {
        Path filePath = Paths.get(FILE_PATH_STR);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("채널 파일 저장 중 오류 발생", e);
        }
    }

    @Override
    public void save(Channel channel) {
        store.put(channel.getId(), channel);
        saveData(store);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
        saveData(store);
    }
}