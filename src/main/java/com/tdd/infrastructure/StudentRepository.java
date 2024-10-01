package com.tdd.infrastructure;

import com.tdd.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Long findByStudentId(long studentId);
    List<Student> findAllByStudentId(long studentId);
}
