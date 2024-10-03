package com.tdd.presentation;

import com.tdd.application.LectureApplyService;
import com.tdd.application.command.LectureCommand;
import com.tdd.domain.exception.BusinessException;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import com.tdd.infrastructure.LectureJpaAdaptor;
import com.tdd.infrastructure.StudentJpaAdaptor;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LectureIntegrationTest {

    @Autowired
    private LectureJpaAdaptor lectureJpaAdaptor; //lock걸려있을 때 목객체


    @Autowired
    private LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;

    @Autowired
    private LectureApplyService lectureApplyService;

    private Lecture mockLecture;

    @BeforeEach
    void setUp() {
        lectureHistoryJpaAdaptor.deleteAll();
    }
    @Test
    @DisplayName("🔴특강 신청 통합테스트 - 수용 인원 초과(30명이 정원이지만 40명이 수강신청 한 상태)")
    void applyLectureIntegrationTest_OverCapacity() throws InterruptedException {
        //given
        final long lectureId = 1L;
        final int enrollCnt = 40;

        CountDownLatch latch = new CountDownLatch(enrollCnt); // 40명의 스레드를 기다림

        ExecutorService executorService = Executors.newFixedThreadPool(40);

            for (int i = 1; i < enrollCnt+1; i++) {
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

        Thread.sleep(1000);

        assertEquals(lectureHistoryJpaAdaptor.countByLectureId(lectureId), 30);
    }


    @Test
    @DisplayName("🔴특강 신청 통합테스트 - 한명이 같은 특강 신청 요청을 5번 연속으로 했을 때")
    void applyLectureIntegrationTest_AlreadyApplied() throws InterruptedException {
        //given
        final long lectureId = 2L;
        final int requestCnt = 5;

        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failureCnt = new AtomicInteger();

        try (ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            CountDownLatch latch = new CountDownLatch(requestCnt);

            for (int i = 0; i < requestCnt; i++) {
                executorService.submit(() -> {
                    try {
                        lectureApplyService.apply(new LectureCommand.Apply(1L, lectureId));
                        successCnt.incrementAndGet();
                    } catch (BusinessException e) {
                        System.out.println("시도 : " + latch.getCount() + " lectureId : " + lectureId + " " + e.getMessage());
                        failureCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(); // 모든 스레드가 완료될 때까지 대기
            executorService.shutdown();
        }
        Thread.sleep(1000);

        assertEquals(1,successCnt.get());
        assertEquals(4,failureCnt.get());

    }
}
