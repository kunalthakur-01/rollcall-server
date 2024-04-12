package com.rollcall.server.services.group_services;

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
import com.rollcall.server.dto.GroupDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceAlreadyExistException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;
import com.rollcall.server.models.Lecture;
import com.rollcall.server.models.User;

@Service
public class GroupServicesImpl implements GroupServices {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Autowired
    private LectureDao lectureDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public GroupDto createNewGroup(GroupDto groupDto, UUID adminId) {
        Group group = dtoToGroup(groupDto);
        User existingUser = null;
        Coordinator existingCoordinator = null;

        try {
            existingUser = userDao.findById(adminId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", String.format("%s", adminId)));
            existingCoordinator = coordinatorDao.findByUser(existingUser);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (existingCoordinator == null) {
            throw new CustomException("Students are not allowed to create the group", 400);
        }

        Group newGroup = null;

        try {
            newGroup = groupDao.findByCoordinatorAndGroupName(existingCoordinator, group.getGroupName());
        } catch (Exception e) {

        }

        if (newGroup != null) {
            throw new ResourceAlreadyExistException("Group already exist with this name");
        }

        try {
            // existingCoordinator.getGroups().add(group); // optional or not compulsary
            group.setCoordinator(existingCoordinator);

            newGroup = groupDao.save(group);

            // List<Group> l = new ArrayList<>();
            // l.add(newGroup);
            // existingCoordinator.setGroups(l);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        // return newGroup;
        return groupToDto(newGroup);
    }

    @Override
    public GroupDto getGroupByLectureId(UUID lectureId) {
        Group existingGroup = null;
        Lecture existingLecture = null;

        try {
            existingLecture = lectureDao.findById(lectureId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId.toString()));
            existingGroup = existingLecture.getGroup();
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        return groupToDto(existingGroup);
    }

    @Override
    public GroupDto getGroupById(UUID groupId) {
        Group existingGroup = null;

        try {
            existingGroup = groupDao.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group", "Id", groupId.toString()));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        
        return groupToDto(existingGroup);
    }

    @Override
    public List<GroupDto> getAllGroups() {
        groupDao.findAll();
        return groupDao.findAll().stream().map(g -> groupToDto(g)).collect(Collectors.toList());
    }

    public GroupDto groupToDto(Group group) {
        GroupDto groupDto = this.modelMapper.map(group, GroupDto.class);
        return groupDto;
    }

    public Group dtoToGroup(GroupDto groupDto) {
        Group group = this.modelMapper.map(groupDto, Group.class);
        return group;
    }
}
