package com.tdd.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data //getter, setter 자동생성
@NoArgsConstructor //기본 생성자 추가
@AllArgsConstructor //모든 필드에 대한 생성자 추가
@Table(name="LECTURE_HISTORY")
@Builder
public class LectureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="history_id")
    private Long historyId;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lecture_id")
    private Lecture lectureId;

    @Column(name="reg_date")
    private LocalDateTime regDate = LocalDateTime.now();

    public LectureHistory(Student studentId, Lecture lectureId) {
        this.studentId = studentId;
        this.lectureId = lectureId;
    }
}
