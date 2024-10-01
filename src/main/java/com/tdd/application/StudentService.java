package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.domain.Student;
import com.tdd.infrastructure.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getStudentHistory(LectureCommand.History command){
        Long studentId = command.getStudentId();
        if(studentId != null){
            return studentRepository.findAllByStudentId(command.getStudentId());
        }
        else{
            return studentRepository.findAll();
        }
    }

}
