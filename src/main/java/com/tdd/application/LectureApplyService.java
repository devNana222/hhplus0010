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
    /** @Transactional
     * 강의 스케줄 엔티티의 현재 수강 신청자 수 증가 및 사용자 특강 신청 완료 히스토리 정보등록을 하나의 트랜잭션으로 묶어 동시성 문제를 처리한다.
     * https://imiyoungman.tistory.com/9
     * Spring APO를 통해 구현되어있다.
     * 클래스, 메소드에 @Transactional이 선언되면 해당 클래스에 트랜잭션이 적용된 프록시 객체 생성
     * 프록시 객체는 @Transactional이 포함된 메서드가 호출될 경우, 트랜잭션을 시작하고 Commit or Rollback을 수행
     * CheckedException or 예외가 없을 때는 Commit
     * UncheckedException이 발생하면 Rollback
    **/
    @Transactional(readOnly = true)
    public void apply(LectureCommand.Apply command){

        Lecture lecture = lectureJpaAdaptor.findByLectureIdWithLock(command.lectureId()).orElseThrow(()-> new BusinessException(INVALID_LECTURE));
        Student student = studentJpaAdaptor.findByStudentId(command.studentId()).orElseThrow(()-> new BusinessException(INVALID_STUDENT));

    //   Long availableCnt = lectureHistoryAdaptor.countByLectureId(command.lectureId());

    //   if (availableCnt <= 0) {
    //       throw new BusinessException(OVERCAPACITY_LECTURE);
    //   }

        if(lecture.getCapacity() <=0){
            throw new BusinessException(OVERCAPACITY_LECTURE);
        }
        else{
            lecture.setCapacity(lecture.getCapacity() - 1);

            // Lecture 업데이트
            lectureJpaAdaptor.save(lecture);
            LectureHistory history = new LectureHistory(student, lecture);

            lectureHistoryJpaAdaptor.save(history);
        }

    }

}
