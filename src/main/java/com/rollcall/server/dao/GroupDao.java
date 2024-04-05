package com.rollcall.server.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Group;
import java.util.UUID;
import com.rollcall.server.models.Coordinator;

public interface GroupDao extends JpaRepository<Group, UUID> {
    public Optional<Group> findById(UUID id);
    public Group findByCoordinatorAndGroupName(Coordinator coordinator, String groupName);
}
