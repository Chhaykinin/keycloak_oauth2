package com.config.authenticationwithkeycloak.service;

import com.config.authenticationwithkeycloak.model.dto.request.UserRequest;
import com.config.authenticationwithkeycloak.model.entity.User;

public interface UserService {
    User getCurrentUser(String name);

    void updateUserByCurrenId(String userId, UserRequest userRequest);
}
