package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.LectureRepository;
import com.tdd.domain.Lecture;
import com.tdd.presentation.dto.LectureDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LectureService {


    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }


    public List<Lecture> getAvailableLectures(LectureCommand.Available command){
        if(command.isAvailable()){
            return lectureRepository.findAllAvailable();
        }
        else{
            return lectureRepository.findAll();
        }
    }



}
