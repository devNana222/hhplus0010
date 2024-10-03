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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
        Lecture lecture = new Lecture(1L, "특강1", 30L, LocalDate.parse("2024-10-01", DateTimeFormatter.ISO_DATE), "강사"); // capacity를 30으로 설정
        Student student = new Student(1L, "학생명1"); // 학생 객체도 초기화


        // when
        when(lectureJpaAdaptor.findByLectureIdWithLock(1L)).thenReturn(Optional.of(lecture));
        when(studentJpaAdaptor.findByStudentId(1L)).thenReturn(Optional.of(student));

        // then
        lectureApplyService.apply(command);

        verify(lectureHistoryJpaAdaptor, times(1)).save(any(LectureHistory.class));
        assertEquals(29L, lecture.getCapacity());
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
        lenient().when(lectureJpaAdaptor.findByLectureIdWithLock(999L)).thenReturn(Optional.empty());

        // then
        assertThrows(BusinessException.class, () -> lectureApplyService.apply(command));

        verify(lectureHistoryJpaAdaptor, never()).save(any(LectureHistory.class));

    }

}