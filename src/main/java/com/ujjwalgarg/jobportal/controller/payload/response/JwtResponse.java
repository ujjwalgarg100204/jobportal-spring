package com.ujjwalgarg.jobportal.controller.payload.response;

/**
 * JwtResponse
 */
public record JwtResponse(
        String token,
        UserJwtResponse user) {
}
