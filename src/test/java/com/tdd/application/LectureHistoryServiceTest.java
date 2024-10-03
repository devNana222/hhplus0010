package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.domain.exception.BusinessException;
import com.tdd.domain.exception.LectureErrorCode;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import com.tdd.infrastructure.StudentJpaAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureHistoryServiceTest {

    @InjectMocks
    private LectureHistoryService lectureHistoryService;

    @Mock
    private StudentJpaAdaptor studentJpaAdaptor;
    @Mock
    private LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;

    private Long validStudentId;
    private Student mockStudent;
    private List<LectureHistory> mockLectureHistory;

    @BeforeEach
    void setUp() {
        validStudentId = 1L;
        mockStudent = new Student(1L, "nana");
        mockLectureHistory = new ArrayList<>();
    }

    @Test
    @DisplayName("🟢특강신청 히스토리를 성공적으로 불러옵니다.")
    void getStudentHistory_SUCCESS() {

        when(studentJpaAdaptor.findByStudentId(validStudentId)).thenReturn(Optional.of(mockStudent));
        when(lectureHistoryJpaAdaptor.findAllByStudentId(mockStudent.getStudentId())).thenReturn(mockLectureHistory);

        LectureCommand.History command = new LectureCommand.History(validStudentId);

        List<LectureHistory> result = lectureHistoryService.getStudentHistory(command);

        assertNotNull(result);
        assertEquals(mockLectureHistory, result);
    }

    @Test
    @DisplayName("🔴히스토리 불러오기 실패 - 존재하지 않는 아이디.")
    void getStudentHistory_FAIL_InvalidStudentId() {
        //given
        when(studentJpaAdaptor.findByStudentId(validStudentId)).thenReturn(Optional.empty());

        //when
        LectureCommand.History command = new LectureCommand.History(validStudentId);

        //then
        BusinessException exception = assertThrows(BusinessException.class, ()->{
            lectureHistoryService.getStudentHistory(command);
        });

        assertEquals(LectureErrorCode.INVALID_STUDENT, exception.getErrorCode());
        verify(studentJpaAdaptor, times(1)).findByStudentId(validStudentId);
        verify(lectureHistoryJpaAdaptor, never()).findAllByStudentId(any());
    }
}