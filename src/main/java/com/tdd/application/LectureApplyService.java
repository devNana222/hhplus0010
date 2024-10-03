package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import com.tdd.infrastructure.LectureJpaAdaptor;
import com.tdd.infrastructure.StudentJpaAdaptor;
import com.tdd.domain.exception.BusinessException;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tdd.domain.exception.LectureErrorCode.*;

@Service
public class LectureApplyService {

    private final LectureJpaAdaptor lectureJpaAdaptor;
    private final StudentJpaAdaptor studentJpaAdaptor;
    private final LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;

    public LectureApplyService(LectureJpaAdaptor lectureJpaAdaptor
                            , StudentJpaAdaptor studentJpaAdaptor
                            , LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor) {
        this.lectureJpaAdaptor = lectureJpaAdaptor;
        this.studentJpaAdaptor = studentJpaAdaptor;
        this.lectureHistoryJpaAdaptor = lectureHistoryJpaAdaptor;
    }

    @Transactional
    public void apply(LectureCommand.Apply command){

        Lecture lecture = lectureJpaAdaptor.findByLectureIdWithLock(command.lectureId()).orElseThrow(()-> new BusinessException(INVALID_LECTURE));
        Student student = studentJpaAdaptor.findByStudentId(command.studentId()).orElseThrow(()-> new BusinessException(INVALID_STUDENT));

        if(lecture.getCapacity() <=0)
            throw new BusinessException(OVERCAPACITY_LECTURE);

        lecture.LectureCapacityReduce();

        LectureHistory history = new LectureHistory(student, lecture);

        Optional<Long> historyId = lectureHistoryJpaAdaptor.findByLectureIdAndStudentId(command.lectureId(), command.studentId());

        if(historyId.isPresent())
          throw new BusinessException(APPLIED_LECTURE);

        lectureHistoryJpaAdaptor.save(history);
    }

}
