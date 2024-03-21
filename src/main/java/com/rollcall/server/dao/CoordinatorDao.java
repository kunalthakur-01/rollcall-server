package com.rollcall.server.dao;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.Coordinator;

public interface CoordinatorDao extends CrudRepository<Coordinator, UUID> {
    // public Coordinator  findByUser(User user);
}
