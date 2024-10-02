package com.tdd.infrastructure;

import com.tdd.domain.*;
import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import com.tdd.infrastructure.jpaEntity.LectureHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor

public class LectureHistoryJpaAdaptor implements LectureHistoryRepository {
    private final LectureHistoryJpaRepository lectureHistoryJpaRepository;

    @Override
    public List<LectureHistory> findAllByStudentId(Student student){
        return lectureHistoryJpaRepository.findAllByStudentId(student);
    }

    @Override
    public Long countByLectureId(long lectureId){
        return lectureHistoryJpaRepository.countByLectureId(lectureId);
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
