package com.tdd.application.command;

import com.tdd.domain.exception.BusinessException;
import com.tdd.domain.exception.LectureErrorCode;

public class LectureCommand {//서비스에서 쓰는 것 >알거라고 생각함 >쫌따가

    public record Apply(Long studentId, Long lectureId){
        public Apply{
            if(studentId == null){
                throw new BusinessException(LectureErrorCode.INVALID_STUDENT);
            }
            if(lectureId == null){
                throw new BusinessException(LectureErrorCode.INVALID_LECTURE);
            }
        }

        public static Apply from(Long studentId, Long LectureId) {
            return new Apply(studentId, LectureId);
        }
    }

    public static class Available {
        private boolean available;

        public Available(boolean available) {
            this.available = available;
        }
        public boolean isAvailable() {
            return available;
        }

    }
    public static class History {
        private final Long studentId;

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
