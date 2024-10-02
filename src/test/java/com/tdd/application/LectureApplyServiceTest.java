package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.domain.exception.BusinessException;
import com.tdd.infrastructure.LectureJpaAdaptor;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import com.tdd.infrastructure.StudentJpaAdaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureApplyServiceTest {

    @InjectMocks
    private LectureApplyService lectureApplyService;

    @Mock
    private LectureJpaAdaptor lectureJpaAdaptor;

    @Mock
    private StudentJpaAdaptor studentJpaAdaptor;

    @Mock
    private LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;

    @AfterEach
    void after(){
        lectureHistoryJpaAdaptor.deleteAll();
    }


    @Test
    @DisplayName("🟢성공적인 특강 신청")
    void applyLectureTest_Success() {
        //given
        LectureCommand.Apply command = new LectureCommand.Apply(1L,1L);
        Lecture lecture = new Lecture();
        Student student = new Student();

        // when
        when(lectureJpaAdaptor.findByLectureId(1L)).thenReturn(Optional.of(lecture));
        when(studentJpaAdaptor.findByStudentId(1L)).thenReturn(Optional.of(student));

        // then
        lectureApplyService.apply(command);

        verify(lectureHistoryJpaAdaptor, times(1)).save(any(LectureHistory.class));

    }

    @Test
    @DisplayName("🔴특강 신청 실패1 - 존재하지 않는 학습자")
    void applyLectureTest1_Fail() {
        //given
        LectureCommand.Apply command = new LectureCommand.Apply(999L,1L);

        // when
        lenient().when(studentJpaAdaptor.findByStudentId(999L)).thenReturn(Optional.empty());

        // then
        assertThrows(BusinessException.class, () -> lectureApplyService.apply(command));

        verify(lectureHistoryJpaAdaptor, never()).save(any(LectureHistory.class));

    }

    @Test
    @DisplayName("🔴특강 신청 실패2 - 존재하지 않는 강의")
    void applyLectureTest2_Fail() {
        //given
        LectureCommand.Apply command = new LectureCommand.Apply(1L,999L);

        // when
        when(lectureJpaAdaptor.findByLectureId(999L)).thenReturn(Optional.empty());

        // then
        assertThrows(BusinessException.class, () -> lectureApplyService.apply(command));

        verify(lectureHistoryJpaAdaptor, never()).save(any(LectureHistory.class));

    }

    @Test
    @DisplayName("🔴특강 신청 실패3 - 수용 인원 초과")
    void applyLectureTest3_Fail() throws InterruptedException {
        //given
        Long lectureId = 1L;
        Long studentId = 2L;
        Long capacity = 30L;

        LectureCommand.Apply command = new LectureCommand.Apply(studentId,lectureId);

        Lecture lecture = new Lecture();
        lecture.setCapacity(capacity);
        // when
        when(lectureJpaAdaptor.findByLectureIdWithLock(lectureId)).thenReturn(Optional.of(lecture));
        when(studentJpaAdaptor.findByStudentId(studentId)).thenReturn(Optional.of(new Student()));

        when(lectureHistoryJpaAdaptor.countByLectureId(lectureId)).thenReturn(capacity); // 이미 30명이 신청한 상태


        ExecutorService executorService = Executors.newFixedThreadPool(40);
        CountDownLatch latch = new CountDownLatch(40); // 40명의 스레드를 기다림
        for (int i = 0; i < 40; i++) {
            executorService.submit(() -> {
                try {
                    lectureApplyService.apply(command);
                } catch (BusinessException e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // Assert
        verify(lectureHistoryJpaAdaptor, times(30)).save(any(LectureHistory.class)); // 30명만 저장되어야 합니다.

        lectureHistoryJpaAdaptor.deleteAll();
    }
}