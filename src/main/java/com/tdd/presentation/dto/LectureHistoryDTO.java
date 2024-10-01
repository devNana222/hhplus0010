package com.tdd.presentation.dto;

import java.time.LocalDateTime;

public class LectureHistoryDTO {
    private String lectureNm;
    private LocalDateTime appliedDate;

    public LectureHistoryDTO(String lectureNm, LocalDateTime appliedDate) {
        this.lectureNm = lectureNm;
        this.appliedDate = appliedDate;
    }

    public String getLectureNm() {
        return lectureNm;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }
}
