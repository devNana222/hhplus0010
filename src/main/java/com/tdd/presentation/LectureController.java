package com.tdd.presentation;

import com.tdd.application.LectureApplyService;
import com.tdd.application.LectureService;
import com.tdd.application.StudentService;
import com.tdd.domain.LectureHistory;
import com.tdd.domain.Student;
import com.tdd.presentation.dto.LectureApplyDTO;
import com.tdd.application.command.LectureCommand;
import com.tdd.domain.Lecture;
import com.tdd.presentation.dto.LectureDTO;
import com.tdd.presentation.dto.LectureHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureApplyService lectureApplyService;
    private final LectureService lectureService;
    private final StudentService studentService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyLecture(@RequestBody LectureApplyDTO lectureApplyDTO) { //request객체와 response객체는 controller에서 사용하므로 presentation이 맞는 것 같다.
        log.info("applyLecture");
        LectureCommand.Apply command = LectureCommand.Apply.from(
                lectureApplyDTO.getStudentId(),
                lectureApplyDTO.getLectureId()
        );
        lectureApplyService.apply(command);
        return ResponseEntity.ok("Lecture application successful.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Lecture>> getAvailableLectures(){
        LectureCommand.Available command = new LectureCommand.Available(true);
        List<Lecture> availableLectures = lectureService.getAvailableLectures(command);
        return ResponseEntity.ok(availableLectures);
    }

    @GetMapping("/list/{studentId}")
    public ResponseEntity<List<Student>> getLectureApplicationHistory(@PathVariable Long studentId){
        LectureCommand.History command = new LectureCommand.History(studentId);
        List<Student> history = studentService.getStudentHistory(command);
        return ResponseEntity.ok(history);
    }
}
