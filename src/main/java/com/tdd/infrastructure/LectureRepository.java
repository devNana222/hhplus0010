package com.tdd.infrastructure;

import com.tdd.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Long findByLectureId(long LectureId);
    @Query("SELECT l FROM Lecture l " +
            "WHERE l.capacity > (SELECT COUNT(h) FROM LectureHistory h WHERE h.lectureId = l)")
    List<Lecture> findAllAvailable();
}
