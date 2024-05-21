package com.rollcall.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.RefreshToken;
import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenDao extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
