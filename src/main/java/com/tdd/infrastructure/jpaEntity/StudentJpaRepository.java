package com.tdd.infrastructure.jpaEntity;

import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(long studentId);
    List<LectureHistory> findAllByStudentId(Student student);
}
