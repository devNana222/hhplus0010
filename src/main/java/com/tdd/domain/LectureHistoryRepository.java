package com.tdd.domain;

import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;

import java.util.List;
import java.util.Optional;

public interface LectureHistoryRepository{
    List<LectureHistory> findAllByStudentId(Long student);

    Long countByLectureId(Long lectureId);

    Optional<Long> findByLectureIdAndStudentId(Long lectureId, Long student);

    LectureHistory save(LectureHistory lecture);

    List<LectureHistory> findAllBylectureId(Long lectureId);

    Long countByLectureIdAndStudentId(Long lectureId, Long studentId);

    void deleteAll();
}
