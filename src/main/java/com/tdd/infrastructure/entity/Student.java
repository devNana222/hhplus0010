package com.tdd.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //getter, setter 자동생성
@NoArgsConstructor //기본 생성자 추가
@AllArgsConstructor //모든 필드에 대한 생성자 추가
@Table(name="STUDENT")
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private Long studentId;

    @Column(name="student_nm", nullable = false)
    private String studentNm;


}
