package com.tdd.presentation;

import com.tdd.application.LectureApplyService;
import com.tdd.application.LectureHistoryService;
import com.tdd.application.LectureService;
import com.tdd.domain.exception.BusinessException;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.presentation.dto.LectureApplyDTO;
import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.entity.Lecture;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/lecture")
@RequiredArgsConstructor
@Validated
public class LectureController {

    private final LectureApplyService lectureApplyService;
    private final LectureService lectureService;
    private final LectureHistoryService lectureHistoryService;

    //특강 신청 API
    @PostMapping("/apply")
    public ResponseEntity<String> applyLecture(@RequestBody LectureApplyDTO lectureApplyDTO) {

        LectureCommand.Apply command = LectureCommand.Apply.from(
                lectureApplyDTO.getStudentId(),
                lectureApplyDTO.getLectureId()
        );
        lectureApplyService.apply(command);
        return ResponseEntity.ok("Lecture application successful.");
    }

    //신청 가능 리스트 조회API
    @GetMapping("/list")
    public ResponseEntity<List<Lecture>> getAvailableLectures(@ModelAttribute @Valid LectureCommand.Date command) {
        //LectureCommand.Date command = new LectureCommand.Date(date);
        List<Lecture> availableLectures = lectureService.getAvailableLectures(command);
        return ResponseEntity.ok(availableLectures);
    }

    //신청 완료 목록 조회 API
    @GetMapping("/{studentId}/list")
    public ResponseEntity<List<LectureHistory>> getLectureApplicationHistory(@PathVariable Long studentId) {
        LectureCommand.History command = new LectureCommand.History(studentId);
        List<LectureHistory> history = lectureHistoryService.getStudentHistory(command);
        return ResponseEntity.ok(history);
    }
}
