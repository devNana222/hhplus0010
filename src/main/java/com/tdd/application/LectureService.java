package com.tdd.application;

import com.tdd.application.command.LectureCommand;
import com.tdd.infrastructure.LectureJpaAdaptor;
import com.tdd.infrastructure.entity.Lecture;
import com.tdd.infrastructure.LectureHistoryJpaAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class LectureService {


    private final LectureJpaAdaptor lectureJpaAdaptor;
    private final LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor;

    public LectureService(LectureJpaAdaptor lectureJpaAdaptor, LectureHistoryJpaAdaptor lectureHistoryJpaAdaptor) {
        this.lectureJpaAdaptor = lectureJpaAdaptor;
        this.lectureHistoryJpaAdaptor = lectureHistoryJpaAdaptor;
    }


    public List<Lecture> getAvailableLectures(LectureCommand.Date command){
        if(command.getDate() != null || command.getDate() != ""){
            LocalDate localDate;
            localDate = LocalDate.parse(command.getDate(), DateTimeFormatter.ISO_DATE);

            return lectureJpaAdaptor.findAvailableLecturesByDate(localDate);
        }
        else{
            return lectureJpaAdaptor.findAll();
        }
    }
}
