package com.rollcall.server.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.models.Group;
import com.rollcall.server.services.group_services.GroupServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user/group")
public class GroupController {
    
    @Autowired
    private GroupServices groupServices;
    
    @PostMapping("/new/{cid}")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody Group group, @PathVariable("cid") UUID adminId){
        Group newGroup = groupServices.createNewGroup(group, adminId);
        return ResponseEntity.status(201).body(newGroup);
        // return null;
    }

    // @GetMapping("/")
    // public ResponseEntity<Group> getGroup(@Valid @RequestBody Group group, @PathVariable("cid") Long adminId){
    //     Group newGroup = groupServices.createNewGroup(group, adminId);
    //     return ResponseEntity.status(201).body(newGroup);
    //     // return null;
    // }
}
