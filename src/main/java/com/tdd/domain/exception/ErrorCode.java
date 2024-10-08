package com.tdd.domain.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getCode();

    HttpStatus getStatus();

    String getMessage();
}
