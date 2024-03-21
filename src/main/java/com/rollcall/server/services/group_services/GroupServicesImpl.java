package com.rollcall.server.services.group_services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.GroupDao;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;

@Service
public class GroupServicesImpl implements GroupServices {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Override
    public Group createNewGroup(Group group, UUID adminId) {
        Coordinator isCoordinatorExist = null;

        try {
            isCoordinatorExist = coordinatorDao.findById(2);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if(isCoordinatorExist == null) {
            throw new ResourceNotFoundException("Coordinator", "Id", String.format("%s", adminId));
        }

        Group newGroup = null;
        try {
            newGroup = groupDao.findById(adminId).orElseThrow(() -> new InternalServerException("not found"));
            // newGroup = groupDao.save(group);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
        
        // System.out.println(isCoordinatorExist);

        return newGroup;
    }
    
}
