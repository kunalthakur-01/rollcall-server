package com.rollcall.server.models;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private UUID id;
    private String jwtToken;
    private String refreshToken;
    private String email;
    private String userName;
    private String profession;
    private long tokenValidity;
}
