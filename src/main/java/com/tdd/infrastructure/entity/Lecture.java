package com.tdd.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Data //getter, setter 자동생성
@NoArgsConstructor //기본 생성자 추가
@AllArgsConstructor //모든 필드에 대한 생성자 추가
@Table(name="LECTURE")
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lecture_id")
    private Long lectureId;

    @Column(name="lecture_nm", nullable = false)
    private String lectureNm;

    @ColumnDefault("30L")
    @Column(name="capacity")
    private Long capacity;

    @Column(name="enroll_start_date")
    private LocalDate enrollStartDate;

    @Column(name="instructor")
    private String instructor;
}
