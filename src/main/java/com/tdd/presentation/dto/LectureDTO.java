package com.tdd.presentation.dto;

public class LectureDTO {
    private long lectureId;
    private String lectureNm;

    public LectureDTO(Long lectureId, String lectureNm) {
        this.lectureId = lectureId;
        this.lectureNm = lectureNm;
    }

    public long getLectureId() {
        return lectureId;
    }
    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }
    public String getLectureNm() {
        return lectureNm;
    }
    public void setLectureNm(String lectureNm) {
        this.lectureNm = lectureNm;
    }
}
