package com.tdd.domain;

import com.tdd.infrastructure.entity.Student;

import java.util.Optional;

public interface StudentRepository{
    Optional<Student> findByStudentId(long studentId);
}
