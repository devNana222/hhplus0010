package com.tdd.application;

import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.LectureJpaAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureJpaAdaptor lectureJpaAdaptor;

    private LocalDate date;
    private Lecture mockLecture1;
    private Lecture mockLecture2;

    @BeforeEach
    void setUp() {
        date = LocalDate.parse("2024-09-30", DateTimeFormatter.ISO_DATE);

        mockLecture1 = new Lecture();
        mockLecture1.setLectureNm("특강1");
        mockLecture1.setCapacity(30L);
        mockLecture1.setEnrollStartDate(date);

        mockLecture2 = new Lecture();
        mockLecture2.setLectureNm("특강2");
        mockLecture2.setCapacity(25L);
        mockLecture2.setEnrollStartDate(date);

        // lectureAdaptor Mock 설정

    }

    @Test
    @DisplayName("🟢유효한 날짜로 특강 조회")
    void getAvailableLectures_SUCCESS_AVAILABLE() {
        LocalDate searchDate = LocalDate.parse("2024-09-30", DateTimeFormatter.ISO_DATE);
        when(lectureJpaAdaptor.findAvailableLecturesByDate(date)).thenReturn(Arrays.asList(mockLecture1, mockLecture2));

        List<Lecture> availableLectures = lectureJpaAdaptor.findAvailableLecturesByDate(searchDate);

        assertNotNull(availableLectures);
        assertEquals(2, availableLectures.size());
        assertEquals("특강1", availableLectures.get(0).getLectureNm());
        assertEquals("특강2", availableLectures.get(1).getLectureNm());
    }

    @Test
    @DisplayName("🟢전체 특강 조회")
    void getAvailableLectures_SUCCESS_ALL() {
        Lecture mockLecture3;
        Lecture mockLecture4;

        mockLecture3 = new Lecture();
        mockLecture3.setLectureNm("특강3");
        mockLecture3.setCapacity(25L);
        mockLecture3.setEnrollStartDate(LocalDate.parse("2024-10-01", DateTimeFormatter.ISO_DATE));

        mockLecture4 = new Lecture();
        mockLecture4.setLectureNm("특강4");
        mockLecture4.setCapacity(5L);
        mockLecture4.setEnrollStartDate(LocalDate.parse("2024-10-02", DateTimeFormatter.ISO_DATE));

        when(lectureJpaAdaptor.findAll()).thenReturn(Arrays.asList(mockLecture1, mockLecture2, mockLecture3, mockLecture4));

        List<Lecture> Lectures = lectureJpaAdaptor.findAll();

        assertNotNull(Lectures);
        assertEquals(4, Lectures.size());
        assertEquals("특강1", Lectures.get(0).getLectureNm());
        assertEquals("특강2", Lectures.get(1).getLectureNm());
        assertEquals("특강3", Lectures.get(2).getLectureNm());
        assertEquals("특강4", Lectures.get(3).getLectureNm());
    }
}