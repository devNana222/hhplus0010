package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.LectureHistoryRepository;
import com.tdd.infrastructure.LectureRepository;
import com.tdd.infrastructure.StudentRepository;
import com.tdd.domain.exception.BusinessException;
import com.tdd.domain.Lecture;
import com.tdd.domain.LectureHistory;
import com.tdd.domain.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tdd.domain.exception.LectureErrorCode.INVALID_LECTURE;
import static com.tdd.domain.exception.LectureErrorCode.INVALID_STUDENT;

@Service
public class LectureApplyService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final LectureHistoryRepository lectureHistoryRepository;

    public LectureApplyService(LectureRepository lectureRepository
                            , StudentRepository studentRepository
                            , LectureHistoryRepository lectureHistoryRepository) {
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.lectureHistoryRepository = lectureHistoryRepository;
    }
    /** @Transactional
     * 강의 스케줄 엔티티의 현재 수강 신청자 수 증가 및 사용자 특강 신청 완료 히스토리 정보등록을 하나의 트랜잭션으로 묶어 동시성 문제를 처리한다.
     * https://imiyoungman.tistory.com/9
     * Spring APO를 통해 구현되어있다.
     * 클래스, 메소드에 @Transactional이 선언되면 해당 클래스에 트랜잭션이 적용된 프록시 객체 생성
     * 프록시 객체는 @Transactional이 포함된 메서드가 호출될 경우, 트랜잭션을 시작하고 Commit or Rollback을 수행
     * CheckedException or 예외가 없을 때는 Commit
     * UncheckedException이 발생하면 Rollback
    **/
    @Transactional

    /**
     *
    * */
    public void apply(LectureCommand.Apply command){
        Lecture lecture = lectureRepository.findById(command.lectureId()).orElseThrow(()-> new BusinessException(INVALID_LECTURE));

        Student student = studentRepository.findById(command.studentId()).orElseThrow(()-> new BusinessException(INVALID_STUDENT));

        LectureHistory history = new LectureHistory(student, lecture);

        lectureHistoryRepository.save(history);
    }

}
