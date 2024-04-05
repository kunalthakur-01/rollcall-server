package com.rollcall.server.services.addMember_services;

import java.util.UUID;

import com.rollcall.server.dto.GroupDto;
import com.rollcall.server.payloads.ApiResponse;

public interface AddMemberServices {
    ApiResponse addCoordinator(UUID userId, UUID groupId);
    GroupDto addAttendee(UUID userId, UUID groupId);
}
