package com.tdd.domain;

import com.tdd.infrastructure.entity.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureRepository{
    Optional<Lecture> findByLectureId(long LectureId);
    List<Lecture> findAll();
    List<Lecture> findAvailableLecturesByDate(LocalDate date);
    Optional<Lecture> findByLectureIdWithLock(long lectureId);
    Lecture save(Lecture lecture);
}
