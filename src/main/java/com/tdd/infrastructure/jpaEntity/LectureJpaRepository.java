package com.tdd.infrastructure.jpaEntity;

import com.tdd.infrastructure.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByLectureId(long LectureId);
    List<Lecture> findAll();

    @Query("SELECT l FROM Lecture l WHERE l.enrollStartDate = :date AND l.capacity > 0")
    List<Lecture> findAvailableLecturesByDate(LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.lectureId = :lectureId")
    Optional<Lecture> findByLectureIdWithLock(long lectureId);

    Lecture save(Lecture lecture);
}
