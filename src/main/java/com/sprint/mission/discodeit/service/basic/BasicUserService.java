package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User read(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String email, String username, String password, String statusMessage) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.update(email, username, password, statusMessage);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}