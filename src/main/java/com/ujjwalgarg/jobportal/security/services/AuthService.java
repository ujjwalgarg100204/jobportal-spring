package com.ujjwalgarg.jobportal.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.service.UserService;

/**
 * AuthService
 */
@Service
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User getAuthenticatedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userService
                .getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Invalid authentication request, user with email:%s does not exist",
                        userDetails.getUsername())));
    }

}
