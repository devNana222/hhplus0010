package com.tdd.infrastructure;

import com.tdd.domain.LectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureHistoryRepository extends JpaRepository<LectureHistory, Long> { //통합테스트하고
    List<LectureHistory> findById(long Id);
}//entity를 domain개체로 converting
//외부 시스템과 통신
