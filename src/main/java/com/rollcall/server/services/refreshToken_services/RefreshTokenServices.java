package com.rollcall.server.services.refreshToken_services;

import com.rollcall.server.models.RefreshToken;

public interface RefreshTokenServices {
    RefreshToken createRefreshToken(String username);
    RefreshToken verifyRefreshToken(String refreshToken);
}
