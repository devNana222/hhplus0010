package com.tdd.infrastructure;

import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.domain.StudentRepository;
import com.tdd.infrastructure.jpaEntity.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class StudentJpaAdaptor implements StudentRepository {
    private final StudentJpaRepository studentJpaRepository;

    @Override
    public Optional<Student> findByStudentId(long studentId){
        return studentJpaRepository.findByStudentId(studentId);
    }
}
