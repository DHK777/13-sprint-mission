package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User create(String email, String username, String password, MultipartFile profile);

  User find(UUID id);

  List<User> findAll();

  User update(UUID id, String newEmail, String newUsername, String newPassword,
      String statusMessage, MultipartFile profile);

  void delete(UUID id);

  List<UserDto> findAllUsers();
}
