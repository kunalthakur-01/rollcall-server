package com.rollcall.server.services.addMember_services;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.payloads.ApiResponse;

public interface AddMemberServices {
    ApiResponse addCoordinator(UUID userId, UUID groupId);
    ApiResponse addAttendee(UUID userId, UUID groupId);
    ApiResponse addMembers(UUID groupId, List<UUID> members);
}
