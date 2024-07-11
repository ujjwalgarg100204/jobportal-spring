package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.entity.User;

public interface AuthService {

  User getAuthenticatedUser();

  String loginUser(String email, String password);

}
