package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private UserService userService;
  @MockitoBean
  private UserStatusService userStatusService;
  @MockitoBean
  private JpaMetamodelMappingContext jpaMappingContext;

  @Test
  @DisplayName("getAllUsers - 성공: 유저 목록을 정상적으로 조회")
  void getAllUsers_Success() throws Exception {
    // Given
    UserDto mockUser1 = new UserDto(UUID.randomUUID(), "tester1", "test1@test.com", null, true);
    UserDto mockUser2 = new UserDto(UUID.randomUUID(), "tester2", "test2@test.com", null, false);

    given(userService.findAllUsers()).willReturn(List.of(mockUser1, mockUser2));

    // When & Then
    mockMvc.perform(get("/api/users"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].username").value("tester1"))
        .andExpect(jsonPath("$[1].email").value("test2@test.com"));
  }

  @Test
  @DisplayName("deleteUser - 실패: 존재하지 않는 유저 삭제 시 400 상태코드를 반환")
  void deleteUser_Fail() throws Exception {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();
    willThrow(new IllegalArgumentException("유효하지 않은 유저 ID입니다."))
        .given(userService).delete(nonExistentUserId);

    // When & Then
    mockMvc.perform(delete("/api/users/{userId}", nonExistentUserId))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("유효하지 않은 유저 ID입니다."));
  }
}