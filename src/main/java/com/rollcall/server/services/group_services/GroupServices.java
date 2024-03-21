package com.rollcall.server.services.group_services;

import java.util.UUID;

import com.rollcall.server.models.Group;

public interface GroupServices {
    public Group createNewGroup(Group group, UUID attendeeId);
}
