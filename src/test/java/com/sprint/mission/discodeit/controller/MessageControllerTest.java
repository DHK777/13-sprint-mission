package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMappingContext;

  @Test
  @DisplayName("getMessage - 성공: 단건 메시지를 정상적으로 조회")
  void getMessage_Success() throws Exception {
    // Given
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    String content = "Slice Test Message";

    MessageDto mockResponse = new MessageDto(messageId, Instant.now(), Instant.now(), content,
        channelId, null, Collections.emptyList());
    given(messageService.read(messageId)).willReturn(mockResponse);

    // When & Then
    mockMvc.perform(get("/api/messages/{messageId}", messageId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value(content))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()));
  }

  @Test
  @DisplayName("deleteMessage - 실패: 존재하지 않는 메시지 삭제 시 400 반환")
  void deleteMessage_Fail() throws Exception {
    // Given
    UUID messageId = UUID.randomUUID();
    willThrow(new IllegalArgumentException("메시지를 삭제할 수 없습니다."))
        .given(messageService).delete(messageId);

    // When & Then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("메시지를 삭제할 수 없습니다."));
  }
}