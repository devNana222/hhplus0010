package com.tdd.application.command;

import com.tdd.domain.exception.BusinessException;
import com.tdd.domain.exception.LectureErrorCode;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LectureCommand {

    public record Apply(Long studentId, Long lectureId){
        public Apply{
            if(studentId == null || studentId == 0L){
                throw new BusinessException(LectureErrorCode.INVALID_STUDENT);
            }
            if(lectureId == null|| lectureId == 0L){
                throw new BusinessException(LectureErrorCode.INVALID_LECTURE);
            }
        }

        public static Apply from(Long studentId, Long LectureId) {
            return new Apply(studentId, LectureId);
        }
    }

    public static class Date {
        @Pattern(regexp = "^$|\\\\d{4}-\\\\d{2}-\\\\d{2}", message = "날짜 형식이 잘못되었습니다. yyyy-MM-dd 형식이어야 합니다.")
        @Size(max = 10, message = "날짜는 최대 10자까지 입력 가능합니다.")
        private final String date;

        public Date(String date) {
            this.date = date;
        }
        public String getDate() {
            return date;
        }
    }

    public static class History {
        private final Long studentId; // 학생 ID를 저장할 필드

        public History(Long studentId) {
            this.studentId = studentId;
        }

        public static History from(Long studentId) {
            return new History(studentId);
        }

        public Long getStudentId() {
            return studentId;
        }
    }
}
