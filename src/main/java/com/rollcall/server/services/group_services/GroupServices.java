package com.rollcall.server.services.group_services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rollcall.server.dto.GroupDto;
import com.rollcall.server.models.Group;

// import com.rollcall.server.dto.GroupDto;

public interface GroupServices {
    public GroupDto createNewGroup(GroupDto groupDto, UUID attendeeId);
    public List<GroupDto> getAllGroups();
    public GroupDto getGroupByLectureId(UUID lectureId);
    public GroupDto getGroupById(UUID groupId);
    public Map<String, List<Group>> getAllGroupsById(UUID userId, String profession);
}
