package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockitoBean
  private ChannelService channelService;
  @MockitoBean
  private JpaMetamodelMappingContext jpaMappingContext;

  @Test
  @DisplayName("createPublicChannel - 성공: 올바른 요청 시 201 상태코드와 채널 정보를 반환")
  void createPublicChannel_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    String name = "general";
    String desc = "General chat";

    Map<String, String> request = Map.of("name", name, "description", desc);

    ChannelDto mockResponse = new ChannelDto(channelId, ChannelType.PUBLIC, name, desc,
        Collections.emptyList(), null);
    given(channelService.createPublic(name, desc)).willReturn(mockResponse);

    // When & Then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(jsonPath("$.description").value(desc));
  }

  @Test
  @DisplayName("deleteChannel - 실패: 존재하지 않는 채널 삭제 요청 시 400 에러를 반환")
  void deleteChannel_Fail_NotFound() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    willThrow(new IllegalArgumentException("존재하지 않는 채널입니다."))
        .given(channelService).delete(channelId);

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("존재하지 않는 채널입니다."));
  }
}