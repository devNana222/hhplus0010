package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.domain.exception.BusinessException;
import com.tdd.domain.exception.LectureErrorCode;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import com.tdd.infrastructure.StudentJpaAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LectureHistoryService {
    private final LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;
    private final StudentJpaAdaptor studentJpaAdaptor;

    public LectureHistoryService(LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor, StudentJpaAdaptor studentJpaAdaptor) {
        this.lectureHistoryJpaAdaptor = lectureHistoryJpaAdaptor;
        this.studentJpaAdaptor = studentJpaAdaptor;
    }


    public List<LectureHistory> getStudentHistory(LectureCommand.History command) {
        Long studentId = command.getStudentId(); // History에서 studentId 가져오기

        // 학생 ID로 Student 조회
        Student student = studentJpaAdaptor.findByStudentId(studentId)
                .orElseThrow(() -> new BusinessException(LectureErrorCode.INVALID_STUDENT));

        return lectureHistoryJpaAdaptor.findAllByStudentId(student);
    }

}
