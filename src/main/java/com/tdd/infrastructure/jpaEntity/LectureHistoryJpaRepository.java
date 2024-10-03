package com.tdd.infrastructure.jpaEntity;

import com.tdd.infrastructure.entity.LectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {
    @Query("SELECT h.historyId FROM LectureHistory h WHERE h.student.studentId = :studentId")
    List<LectureHistory> findAllByStudentId(Long studentId);

    @Query("SELECT count(h.historyId) FROM LectureHistory h WHERE h.lecture.lectureId = :lectureId")
    Long countByLectureId(@Param("lectureId") Long lectureId);

    @Query("SELECT h.historyId FROM LectureHistory h WHERE h.lecture.lectureId = :lectureId and h.student.studentId = :studentId")
    Optional<Long> findByLectureIdAndStudentId(@Param("lectureId") Long lectureId, @Param("studentId") Long studentId);

    @Query("SELECT h FROM LectureHistory h WHERE h.lecture.lectureId = :lectureId")
    List<LectureHistory> findAllBylectureId(@Param("lectureId") Long lectureId);
    void deleteAll();
}