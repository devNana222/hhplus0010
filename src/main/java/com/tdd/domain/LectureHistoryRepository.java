package com.tdd.domain;

import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;

import java.util.List;

public interface LectureHistoryRepository{
    List<LectureHistory> findAllByStudentId(Student student);
    Long countByLectureId(long lectureId);
    LectureHistory save(LectureHistory lecture);

    void deleteAll();
}
