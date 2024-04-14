package com.rollcall.server.services.addMember_services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rollcall.server.dao.AttendeeDao;
import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.GroupDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.MultipleException;
import com.rollcall.server.exceptions.ResourceAlreadyExistException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;
import com.rollcall.server.models.User;
import com.rollcall.server.payloads.ApiResponse;

@Service
public class AddMemberServicesImpl implements AddMemberServices {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Autowired
    private AttendeeDao attendeeDao;

    @Override
    @Transactional
    public ApiResponse addCoordinator(UUID userId, UUID groupId) {
        Group existingGroup = null;
        Coordinator existingCoordinator = null;

        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
            existingCoordinator = coordinatorDao.findByUser(user);
            existingGroup = groupDao.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group", "Id", userId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (existingCoordinator == null) {
            throw new CustomException("Invalid user", 404);
        }

        if (!existingGroup.getAdmin().equals(existingCoordinator)) {
            Boolean hasInGroup = existingGroup.getCoordinators().contains(existingCoordinator);

            if (hasInGroup) {
                throw new ResourceAlreadyExistException("User already is in the group");
            }
        } else {
            throw new CustomException("User could'nt added to its own group", 400);
        }

        try {
            existingGroup.getCoordinators().add(existingCoordinator);
            // existingCoordinator.getOtherGroups().add(existingGroup);
            groupDao.save(existingGroup);
            // coordinatorDao.save(existingCoordinator);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        return new ApiResponse("Coordinator added successfully!", true);
        // return this.modelMapper.map(existingGroup, GroupDto.class);
    }

    public ApiResponse addAttendee(UUID userId, UUID groupId) {
        Group existingGroup = null;
        Attendee existingAttendee = null;

        try {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
            existingAttendee = attendeeDao.findByUser(user);
            existingGroup = groupDao.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group", "Id", userId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (existingAttendee == null) {
            throw new CustomException("Invalid user", 404);
        }

        Boolean hasInGroup = existingGroup.getAttendees().contains(existingAttendee);

        if (hasInGroup) {
            throw new ResourceAlreadyExistException("User already is in the group");
        }

        try {
            existingGroup.getAttendees().add(existingAttendee);
            // existingAttendee.getOtherGroups().add(existingGroup);
            groupDao.save(existingGroup);
            // attendeeDao.save(existingAttendee);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        return new ApiResponse("Attendee added successfully!", true);
        // return modelMapper.map(existingGroup, GroupDto.class);
    }

    @Override
    public ApiResponse addMembers(UUID groupId, List<UUID> members) {
        Group existingGroup = null;

        try {
            existingGroup = groupDao.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group", "Id", groupId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        List<String> errors = new ArrayList<>();

        try {
            for (int i = 0; i < members.size(); i++) {
                User user = userDao.findById(members.get(i)).orElse(null);
                if(user == null) {
                    errors.add(new ResourceNotFoundException("User", "Id", members.get(i).toString()).getMessage());
                }
                else if(user.getProfession().equals("student")) {
                    Attendee existingAttendee = attendeeDao.findByUser(user);
                    if(!existingGroup.getAttendees().contains(existingAttendee)) {
                        existingGroup.getAttendees().add(existingAttendee);
                    }
                }
                else if(user.getProfession().equals("teacher")){
                    Coordinator existingCoordinator = coordinatorDao.findByUser(user);
                    if(!existingGroup.getCoordinators().contains(existingCoordinator)) {
                        existingGroup.getCoordinators().add(existingCoordinator);
                    }
                }
            }
            
            groupDao.save(existingGroup);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if(!errors.isEmpty()) {
            throw new MultipleException(errors);
        }
        else {
            return new ApiResponse("Members added successfully!", true); 
        }

        // return modelMapper.map(groupDao.save(existingGroup), GroupDto.class);
    }
}
