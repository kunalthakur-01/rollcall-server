package com.rollcall.server.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Group;
import java.util.UUID;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Lecture;


public interface GroupDao extends JpaRepository<Group, UUID> {
    public Group findByAdminAndGroupName(Coordinator coordinator, String groupName);
    public Lecture findByLectures(Lecture lecture);
}
