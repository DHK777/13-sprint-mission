package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("통합: 사용자 생성 - 성공 (프로파일 이미지 없이)")
  void createUser_Success() throws Exception {
    UserCreateRequest requestDto = new UserCreateRequest("new@test.com", "new_user", "password123");
    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDto)
    );

    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("new@test.com"))
        .andExpect(jsonPath("$.username").value("new_user"));
  }

  @Test
  @DisplayName("통합: 사용자 생성 - 실패 (중복된 이메일)")
  void createUser_Fail_Duplicate() throws Exception {
    userRepository.save(new User("dup@test.com", "tester1", "password123"));

    UserCreateRequest requestDto = new UserCreateRequest("dup@test.com", "tester2", "password123");
    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDto)
    );

    mockMvc.perform(multipart("/api/users")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("DUPLICATE_EMAIL"));
  }

  @Test
  @DisplayName("통합: 사용자 목록 조회 - 성공")
  void getAllUsers_Success() throws Exception {
    userRepository.save(new User("list1@test.com", "list1", "password123"));
    userRepository.save(new User("list2@test.com", "list2", "password123"));

    mockMvc.perform(get("/api/users"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @DisplayName("통합: 사용자 삭제 - 성공")
  void deleteUser_Success() throws Exception {
    User user = userRepository.save(new User("del@test.com", "delUser", "password123"));

    mockMvc.perform(delete("/api/users/{userId}", user.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());
  }
}