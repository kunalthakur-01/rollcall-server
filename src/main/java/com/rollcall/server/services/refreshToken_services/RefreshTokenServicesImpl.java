package com.rollcall.server.services.refreshToken_services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.RefreshTokenDao;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.models.RefreshToken;
import com.rollcall.server.models.User;

@Service
public class RefreshTokenServicesImpl implements RefreshTokenServices {

    private long refreshTokenValidity = 60 * 1000;

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public RefreshToken createRefreshToken(String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .expiry(Instant.now().plusMillis(refreshTokenValidity))
                .user(user)
                .build();
        }

        else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }
        
        try {
            user.setRefreshToken(refreshToken);
            refreshTokenDao.save(refreshToken);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        return refreshToken;
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshToken2 = null;
        try {
            refreshToken2 = refreshTokenDao.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Given token doesn't exist in database"));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (refreshToken2.getExpiry().compareTo(Instant.now()) < 0) {
            
            try {
                refreshTokenDao.delete(refreshToken2);
            } catch (Exception e) {
                throw new InternalServerException(e.getMessage());
            }
            
            throw new RuntimeException("Refresh token expired!!");
        }

        return refreshToken2;
    }
}
