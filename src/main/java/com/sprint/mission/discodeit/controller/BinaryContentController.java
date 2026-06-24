package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    @GetMapping("/api/binaryContent/find")
    public ResponseEntity<BinaryContent> findBinaryContent(
            @RequestParam("binaryContentId") UUID binaryContentId) {
        BinaryContent content = binaryContentService.findEntity(binaryContentId);
        return ResponseEntity.ok(content);
    }
}
