package com.rollcall.server.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.User;
import java.util.List;


public interface UserDao extends CrudRepository<User, UUID> {
    public User findByEmail(String email);
    public User findByEmailOrUserNameOrPhone(String email, String userName, long phone);
    public List<User> findByIdNotIn(List<UUID> alreadyAddedUsers);
    public List<User> findByUserNameContainingOrNameContaining(String username, String name);
    public List<User> findByIdNotInAndUserNameContainingOrNameContaining(List<UUID> alreadyAddedUsers, String userName, String name);
}
