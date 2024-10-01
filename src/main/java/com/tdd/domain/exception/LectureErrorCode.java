package com.tdd.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LectureErrorCode implements ErrorCode{
    INVALID_STUDENT(HttpStatus.BAD_REQUEST, "존재하지 않는 학생입니다."),
    INVALID_LECTURE(HttpStatus.BAD_REQUEST, "존재하지 않는 강의입니다."),
    OVERCAPACITY_LECTURE(HttpStatus.NOT_ACCEPTABLE, "정원 초과 된 강의입니다."),
    APPLIED_LECTURE(HttpStatus.NOT_ACCEPTABLE, "이미 신청 된 강의입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
