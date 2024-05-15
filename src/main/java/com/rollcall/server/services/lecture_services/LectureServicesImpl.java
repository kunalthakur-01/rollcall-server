package com.rollcall.server.services.lecture_services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rollcall.server.dao.AttendeeDao;
import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.GroupDao;
import com.rollcall.server.dao.LectureDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Attendee;
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
    private AttendeeDao attendeeDao;

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
            exitingUser = userDao.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
            existingGroup = groupDao.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group", "Id", String.format("%s", groupId)));
            existingCoordinator = coordinatorDao.findByUser(exitingUser);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (existingCoordinator == null) {
            throw new CustomException("Invalid Coordinator Id", 400);
        }

        Boolean isContain = existingGroup.getCoordinators().contains(existingCoordinator);

        if (existingCoordinator != existingGroup.getAdmin() && !isContain) {
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
    public List<Lecture> getLecturesByUserId(UUID userId, String onDate) {
        User existingUser = null;
        Coordinator existingCoordinator = null;
        Attendee existingAttendee = null;

        try {
            existingUser = userDao.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        String profession = existingUser.getProfession();

        if(profession.equals("teacher")) {
            try {
                existingCoordinator = coordinatorDao.findByUser(existingUser);
            } catch (Exception e) {
                throw new InternalServerException(e.getMessage());
            }

            if (existingCoordinator == null)
                throw new ResourceNotFoundException("Coordinator", "Id", userId.toString());
            
            return existingCoordinator.getLectures();
        }

        else {
            try {
                existingAttendee = attendeeDao.findByUser(existingUser);
            } catch (Exception e) {
                throw new InternalServerException(e.getMessage());
            }

            if (existingAttendee == null)
                throw new ResourceNotFoundException("Attendee", "Id", userId.toString());

            List<Lecture> lectures = new ArrayList<>();
            for (Group group : existingAttendee.getOtherGroups()) {
                for (Lecture lecture : group.getLectures()) {
                    lectures.add(lecture);
                }
            }

            return lectures;
        }

        

        // String dateString = onDate.split("T")[0];

        // int year = Integer.parseInt(dateString.split("-")[0]);
        // int month = Integer.parseInt(dateString.split("-")[1]);
        // int date = Integer.parseInt(dateString.split("-")[2]);
        // System.out.println(date);
        // System.out.println(month);
        // System.out.println(year);


        // System.out.println(date.split("-")[0]); 
        // System.out.println(date.split("-")[1]); 
        // System.out.println(date.split("-")[2]); 
            
        // DateTimeFormatter dtf = DateTimeFormatter.RFC_1123_DATE_TIME;
        // LocalDateTime ldt = LocalDateTime.parse(onDate, dtf);

        // System.out.println(ldt);

        // Calendar calendar = Calendar.getInstance();
        // List<Lecture> allLectures = new ArrayList<>();

        // for (Lecture lecture : existingCoordinator.getLectures()) {
        //     calendar.setTime(lecture.getCreatedOnDate());
        //     System.out.println(calendar.get(Calendar.DATE));
        //     System.out.println(date);
        //     System.out.println("*************************");
        //     if(date != calendar.get(Calendar.DATE)) continue;
        //     if(month != calendar.get(Calendar.MONTH) + 1) continue;
        //     if(year != calendar.get(Calendar.YEAR)) continue;

        //     allLectures.add(lecture);
        // }

        // return allLectures;

        // return existingCoordinator.getLectures();
    }

    @Override
    public List<LectureDto> getAllLecture() {
        return lectureDao.findAll().stream().map(l -> lectureToDto(l)).collect(Collectors.toList());
    }

    @Override
    public LectureDto getLectureById(UUID lectId) {
        Lecture existingLecture = null;
        try {
            existingLecture = lectureDao.findById(lectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        return lectureToDto(existingLecture);
    }

    @Override
    public List<Attendee> getAttendeesBylectureId(UUID lectureId) {
        Lecture existingLecture = null;
        try {
            existingLecture = lectureDao.findById(lectureId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        return existingLecture.getGroup().getAttendees();
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
