package com.tdd.infrastructure;

import com.tdd.infrastructure.entity.Lecture;
import com.tdd.domain.LectureRepository;
import com.tdd.infrastructure.jpaEntity.LectureJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class LectureJpaAdaptor implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public Optional<Lecture> findByLectureId(long lectureId){
        return lectureJpaRepository.findByLectureId(lectureId);
    }

    @Override
    public List<Lecture> findAll(){
        return lectureJpaRepository.findAll();
    }

    @Override
    public List<Lecture> findAvailableLecturesByDate(LocalDate date){
        return lectureJpaRepository.findAvailableLecturesByDate(date);
    }

    @Override
    public Optional<Lecture> findByLectureIdWithLock(long lectureId){
        return lectureJpaRepository.findByLectureIdWithLock(lectureId);
    }

    @Override
    public Lecture save(Lecture lecture){
        return lectureJpaRepository.save(lecture);
    }

}
