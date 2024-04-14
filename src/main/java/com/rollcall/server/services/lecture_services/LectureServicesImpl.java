package com.rollcall.server.services.lecture_services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.GroupDao;
import com.rollcall.server.dao.LectureDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;
import com.rollcall.server.models.Lecture;
import com.rollcall.server.models.User;
import com.rollcall.server.payloads.ApiResponse;

@Service
public class LectureServicesImpl implements LectureServices {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LectureDao lectureDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ApiResponse createLecture(LectureDto lectureDto, UUID groupId, UUID userId) {
        Lecture lecture = dtoToLecture(lectureDto);

        Group existingGroup = null;
        User exitingUser = null;
        Coordinator existingCoordinator = null;

        try {
            exitingUser = userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
            existingGroup = groupDao.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group", "Id", String.format("%s", groupId)));
            existingCoordinator = coordinatorDao.findByUser(exitingUser);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if(existingCoordinator == null) {
            throw new CustomException("Invalid Coordinator Id", 400);
        }

        Boolean isContain = existingGroup.getCoordinators().contains(existingCoordinator);
        
        if(existingCoordinator != existingGroup.getAdmin() && !isContain) {
            throw new CustomException("Coordinator not in the group", 400);
        }

        try {
            lecture.setCoordinator(existingCoordinator);
            lecture.setGroup(existingGroup);
            lectureToDto(lectureDao.save(lecture));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        return new ApiResponse("Lecture created successfully!", true); 
    }

    @Override
    public List<LectureDto> getAllLecture() {
        return lectureDao.findAll().stream().map(l -> lectureToDto(l)).collect(Collectors.toList());
    }

    @Override
    public LectureDto getLectureById(UUID lectId) {
        Lecture existingLecture = null;
        try {
            existingLecture = lectureDao.findById(lectId).orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        return lectureToDto(existingLecture);
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
