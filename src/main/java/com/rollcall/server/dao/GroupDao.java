package com.rollcall.server.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.Group;
import java.util.UUID;


public interface GroupDao extends CrudRepository<Group, UUID> {
    public Optional<Group> findById(UUID id);
}
