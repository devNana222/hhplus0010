package com.tdd.infrastructure;

import com.tdd.domain.*;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.infrastructure.jpaEntity.LectureHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class LectureHistoryJpaAdaptor implements LectureHistoryRepository {
    private final LectureHistoryJpaRepository lectureHistoryJpaRepository;

    @Override
    public List<LectureHistory> findAllByStudentId(Long student){
        return lectureHistoryJpaRepository.findAllByStudentId(student);
    }
    @Override
    public List<LectureHistory> findAllBylectureId(Long lectureId){
        return lectureHistoryJpaRepository.findAllBylectureId(lectureId);
    }

    @Override
    public Long countByLectureId(Long lectureId){
        return lectureHistoryJpaRepository.countByLectureId(lectureId);
    }

    @Override
    public Optional<Long> findByLectureIdAndStudentId(Long lectureId, Long studentId){
        return lectureHistoryJpaRepository.findByLectureIdAndStudentId(lectureId, studentId);
    }

    @Override
    public LectureHistory save(LectureHistory lecture){
        return lectureHistoryJpaRepository.save(lecture);
    }

    @Override
    public void deleteAll(){
        lectureHistoryJpaRepository.deleteAll();
    }
}
