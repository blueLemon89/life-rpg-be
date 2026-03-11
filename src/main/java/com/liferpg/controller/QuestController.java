package com.liferpg.controller;

import com.liferpg.service.quest.IQuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/quest")
@RequiredArgsConstructor
public class QuestController {

    private final IQuestService questService;

    @PostMapping("/complete")
    public ResponseEntity<Object> questComplete(@RequestParam(name = "characterQuestId", required = true) UUID id) {
        var result = questService.completeQuest(id);

        return ResponseEntity.ok(result);
    }
}
