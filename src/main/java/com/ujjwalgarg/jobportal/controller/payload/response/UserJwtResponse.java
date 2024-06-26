package com.ujjwalgarg.jobportal.controller.payload.response;

import com.ujjwalgarg.jobportal.constant.ERole;

import lombok.Builder;

/**
 * UserJwtResponse
 */
@Builder
public record UserJwtResponse(
        Integer id,
        String email,
        ERole role) {
}
