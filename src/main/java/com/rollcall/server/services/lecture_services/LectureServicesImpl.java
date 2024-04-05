package com.rollcall.server.services.lecture_services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.GroupDao;
import com.rollcall.server.dao.LectureDao;
import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;
import com.rollcall.server.models.Lecture;

@Service
public class LectureServicesImpl implements LectureServices {

    @Autowired
    private LectureDao lectureDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LectureDto createLecture(LectureDto lectureDto, UUID groupId, UUID coordinatorId) {
        Lecture lecture = dtoToLecture(lectureDto);

        Group existingGroup = null;
        Coordinator existingCoordinator = null;

        try {
            existingGroup = groupDao.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group", "Id", String.format("%s", groupId)));
            existingCoordinator = coordinatorDao.findById(coordinatorId).orElseThrow(() -> new ResourceNotFoundException("Coordinator", "Id", String.format("%s", coordinatorId)));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        
        if(existingCoordinator != existingGroup.getCoordinator()) {
            throw new CustomException("Coordinator not in the group", 400);
        }
        return lectureToDto(lectureDao.save(lecture));
    }

    public LectureDto lectureToDto(Lecture lecture) {
        LectureDto lectureDto = this.modelMapper.map(lecture, LectureDto.class);
        return lectureDto;
    }

    public Lecture dtoToLecture(LectureDto lectureDto) {
        Lecture lecture = this.modelMapper.map(lectureDto, Lecture.class);
        return lecture;
    }
    
}
