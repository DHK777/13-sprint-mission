package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUserService implements UserService {
    private static final String FILE_PATH_STR = "users.dat";

    @SuppressWarnings("unchecked") // 경고 무시
    private Map<UUID, User> loadData() {
        Path filePath = Paths.get(FILE_PATH_STR);
        if (!Files.exists(filePath)) {
            return new HashMap<>(); // 파일이 없으면 빈 맵 반환
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, User> data) {
        Path filePath = Paths.get(FILE_PATH_STR);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 치명적 오류 발생", e);
        }
    }

    @Override
    public User create(User user) {
        Map<UUID, User> data = loadData();
        data.put(user.getId(), user);
        saveData(data);
        return user;
    }

    @Override
    public User read(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(loadData().values());
    }

    @Override
    public User update(UUID id, String username, String email, String password) {
        Map<UUID, User> data = loadData();
        User user = data.get(id);

        if (user != null) {
            user.update(username, email, password);
            saveData(data); //
        }
        return user;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = loadData();
        data.remove(id);
        saveData(data);
    }
}
