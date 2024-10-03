package com.tdd.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.application.LectureApplyService;
import com.tdd.application.LectureHistoryService;
import com.tdd.application.LectureService;
import com.tdd.application.command.LectureCommand;
import com.tdd.domain.exception.BusinessException;
import com.tdd.domain.exception.LectureErrorCode;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.presentation.dto.LectureApplyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private LectureController lectureController;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private LectureApplyService lectureApplyService;

    @MockBean
    private LectureHistoryService lectureHistoryService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화/역직렬화를 위한 ObjectMapper

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("🟢성공적인 특강 신청")
    void applyLecture_Success() throws Exception {
        //given
        LectureApplyDTO dto = new LectureApplyDTO();
        dto.setLectureId(1L);
        dto.setStudentId(1L);
        //when
        doNothing().when(lectureApplyService).apply(LectureCommand.Apply.from(1L,1L));

        //then
        mockMvc.perform(post("/lecture/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Lecture application successful."));
    }

    @Test
    @DisplayName("🔴특강신청 실패 - 잘못된 studentID")
    void applyLecture_FAIL_INVALID_USER() throws NullPointerException, BusinessException {
        // given
        LectureApplyDTO lectureApplyDTO = new LectureApplyDTO();
        lectureApplyDTO.setLectureId(1L);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            lectureController.applyLecture(lectureApplyDTO);
        });

        // 예외 메시지 및 에러 코드 확인
        assertEquals(LectureErrorCode.INVALID_STUDENT, exception.getErrorCode());
        assertEquals("존재하지 않는 학생입니다.", exception.getMessage());

        verify(lectureApplyService, never()).apply(any());
    }

    @Test
    @DisplayName("🔴특강신청 실패 - 잘못된 lectureID")
    void applyLecture_FAIL_INVALID_LECTURE() throws NullPointerException, BusinessException {
        // given
        LectureApplyDTO lectureApplyDTO = new LectureApplyDTO();
        lectureApplyDTO.setStudentId(1L); // lectureId는 유효한 값

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            lectureController.applyLecture(lectureApplyDTO);
        });

        // 예외 메시지 및 에러 코드 확인
        assertEquals(LectureErrorCode.INVALID_LECTURE, exception.getErrorCode());
        assertEquals("존재하지 않는 강의입니다.", exception.getMessage());

        verify(lectureApplyService, never()).apply(any());
    }

    @Test
    @DisplayName("🟢특정 날짜의 존재하는 강의 정상 조회")
    void getAvailableLectures_Success() throws Exception {
        List<Lecture> mockLectures;

        mockLectures = new ArrayList<>();
        mockLectures.add(new Lecture(1L, "특강1", 30L, LocalDate.parse("2024-10-03"), "이석범"));
        mockLectures.add(new Lecture(2L, "특강2", 20L, LocalDate.parse("2024-10-03"), "허재"));

        // given
        when(lectureService.getAvailableLectures(any(LectureCommand.Date.class))).thenReturn(mockLectures);

        // when & then

        mockMvc.perform(get("/lecture/list?date=2024-10-03"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lectureNm").value("특강1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lectureNm").value("특강2"));

    }

    @Test
    @DisplayName("🔴잘못된 날짜 조회 형식")
    void getAvailableLectures_Fail_InvalidDateType() throws Exception {

        mockMvc.perform(get("/lecture/list?date=2021440"))
                .andExpect(status().isBadRequest());   // 400 Bad Request 예상
    }

    @Test
    @DisplayName("🟢한 학생의 신청리스트 정상 조회")
    void getLectureApplicationHistory_Success() throws Exception {
        List<LectureHistory> mockLectureHistory;

        Lecture lecture = new Lecture(1L, "특강1", 30L, LocalDate.parse("2024-10-03"), "허재");
        Student student = new Student(1L, "신예진");

        LectureHistory history1 = new LectureHistory(student, lecture);
        history1.setRegDate(LocalDateTime.of(2024, 10, 3, 12, 0));

        Lecture lecture2 = new Lecture(2L, "특강2", 20L, LocalDate.parse("2024-10-05"), "이석범");
        LectureHistory history2 = new LectureHistory(student, lecture2);
        history2.setRegDate(LocalDateTime.of(2024, 10, 5, 10, 0));

        mockLectureHistory = Arrays.asList(history1, history2);

        when(lectureHistoryService.getStudentHistory(any(LectureCommand.History.class)))
                .thenReturn(mockLectureHistory);

        // when & then
        mockMvc.perform(get("/lecture/1/list"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lectureId.lectureNm").value("특강1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].regDate").value("2024-10-03T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lectureId.instructor").value("허재"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lectureId.lectureNm").value("특강2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].regDate").value("2024-10-05T10:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lectureId.instructor").value("이석범"));

    }
}