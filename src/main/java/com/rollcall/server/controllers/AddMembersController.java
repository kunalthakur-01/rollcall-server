package com.rollcall.server.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.payloads.ApiResponse;
import com.rollcall.server.services.addMember_services.AddMemberServices;

@RestController
@RequestMapping("api/user/add/group")
public class AddMembersController {

    @Autowired
    private AddMemberServices addMemberServices;

    @GetMapping("/add-coordinator/{userId}/{groupId}")
    public ResponseEntity<ApiResponse> addCoordinator(@PathVariable("userId") UUID userId, @PathVariable("groupId") UUID groupId) {
        return ResponseEntity.ok().body(addMemberServices.addCoordinator(userId, groupId));
        // return null;
    }

    @GetMapping("/add-attendee/{userId}/{groupId}")
    public ResponseEntity<ApiResponse> addAttendee(@PathVariable("userId") UUID userId, @PathVariable("groupId") UUID groupId) {
        return ResponseEntity.ok().body(addMemberServices.addAttendee(userId, groupId));
        // return null;
    }

    @PostMapping("/add-members/{groupId}")
    public ResponseEntity<?> addMembers(@PathVariable("groupId") UUID groupId, @RequestBody List<UUID> members) {
        return ResponseEntity.ok().body(addMemberServices.addMembers(groupId, members));
        // return null;
    }
}
