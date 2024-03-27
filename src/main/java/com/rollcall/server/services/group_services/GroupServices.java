package com.rollcall.server.services.group_services;

import java.util.UUID;

import com.rollcall.server.models.Group;

// import com.rollcall.server.dto.GroupDto;

public interface GroupServices {
    public Group createNewGroup(Group group, UUID attendeeId);
}
