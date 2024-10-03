package com.tdd.presentation.dto;

import lombok.Getter;

@Getter
public class LectureDTO {
    private long lectureId;
    private String lectureNm;

    public LectureDTO(Long lectureId, String lectureNm) {
        this.lectureId = lectureId;
        this.lectureNm = lectureNm;
    }
}
