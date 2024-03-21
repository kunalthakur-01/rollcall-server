package com.rollcall.server.services.group_services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.GroupDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;
import com.rollcall.server.models.User;

@Service
public class GroupServicesImpl implements GroupServices {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Group createNewGroup(Group group, UUID adminId) {
        Coordinator isCoordinatorExist = null;
        User existingUser = null;

        try {
            // isCoordinatorExist = coordinatorDao.findById(adminId).orElseThrow(() -> new  ResourceNotFoundException("Coordinator", "Id", String.format("%s", adminId)));
            existingUser = userDao.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", String.format("%s", adminId)));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if(!existingUser.getProfession().equals("teacher")) {
            throw new CustomException("Students are not allowed to create the group", 400);
        }

        Group newGroup = null;
        try {
            // newGroup = groupDao.findById(adminId).orElseThrow(() -> new InternalServerException("not found"));
            newGroup = groupDao.save(group);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        
        System.out.println(isCoordinatorExist);

        return newGroup;
    }
    
}
