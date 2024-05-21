package com.rollcall.server.models;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
