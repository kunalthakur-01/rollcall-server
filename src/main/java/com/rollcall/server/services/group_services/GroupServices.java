package com.rollcall.server.services.group_services;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.dto.GroupDto;

// import com.rollcall.server.dto.GroupDto;

public interface GroupServices {
    public GroupDto createNewGroup(GroupDto groupDto, UUID attendeeId);
    public List<GroupDto> getAllGroups();
}
