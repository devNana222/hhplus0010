package com.tdd.presentation.dto;

public class LectureApplyDTO {
    private long lectureId;
    private long studentId;

    public long getLectureId() {
        return lectureId;
    }
    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }
    public long getStudentId() {
        return studentId;
    }
    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }
}
