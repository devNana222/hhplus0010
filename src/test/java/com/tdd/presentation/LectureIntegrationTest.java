package com.tdd.presentation;

import com.tdd.application.LectureApplyService;
import com.tdd.application.command.LectureCommand;
import com.tdd.domain.exception.BusinessException;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import com.tdd.infrastructure.LectureJpaAdaptor;
import com.tdd.infrastructure.StudentJpaAdaptor;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class LectureIntegrationTest {


    @MockBean
    private LectureJpaAdaptor lectureJpaAdaptor; //lock걸려있을 때 목객체

    @Autowired
    private StudentJpaAdaptor  studentJpaAdaptor;

    @MockBean
    private LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;

    @Autowired
    private LectureApplyService lectureApplyService;


    @BeforeEach
    void setUp() {
        lectureHistoryJpaAdaptor.deleteAll();

        Lecture lecture = new Lecture(1L, "Sample Lecture", 30L, LocalDate.now(), "Instructor"); // Mock 객체 생성
        when(lectureJpaAdaptor.findByLectureIdWithLock(1L)).thenReturn(Optional.of(lecture)); // Mock 동작 정의
    }

    @Test
    @DisplayName("🔴특강 신청 통합테스트 - 수용 인원 초과(30명이 정원이지만 40명이 수강신청 한 상태)")
    void applyLectureIntegrationTest() throws InterruptedException {
        //given
        final long lectureId = 1L;
        final int enrollCnt = 40;

        try (ExecutorService executorService = Executors.newFixedThreadPool(40)) {
            CountDownLatch latch = new CountDownLatch(enrollCnt); // 40명의 스레드를 기다림

            Lecture lecture = lectureJpaAdaptor.findByLectureIdWithLock(lectureId).get();
            lecture.LectureCapacityReduce();

            System.out.println("count : " + lectureHistoryJpaAdaptor.countByLectureId(lecture));
            for (int i = 1; i < 41; i++) {
                Long uniqueStudentId = (long) i;
                executorService.submit(() -> {
                    try {
                        lectureApplyService.apply(new LectureCommand.Apply(uniqueStudentId, lectureId));
                    } catch (BusinessException e) {
                        System.out.println("lectureId : " + lectureId + " " + e.getMessage() + " - studentId : " + uniqueStudentId);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(); // 모든 스레드가 완료될 때까지 대기
            executorService.shutdown();
        }
        Thread.sleep(1000);
        verify(lectureHistoryJpaAdaptor, times(30)).save(any(LectureHistory.class)); // 30명만 저장되어야 합니다.

        lectureHistoryJpaAdaptor.deleteAll();
    }
}
