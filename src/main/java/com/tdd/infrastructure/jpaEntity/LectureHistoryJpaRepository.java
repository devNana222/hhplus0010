package com.tdd.infrastructure.jpaEntity;

import com.tdd.infrastructure.entity.LectureHistory;
import com.tdd.infrastructure.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {
    List<LectureHistory> findAllByStudentId(Student student);

    @Query("SELECT COUNT(h) FROM LectureHistory h WHERE h.lectureId.lectureId = :lectureId")
    Long countByLectureId(@Param("lectureId") long lectureId);
    void deleteAll();
}