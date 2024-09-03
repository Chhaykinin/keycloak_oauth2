package com.config.authenticationwithkeycloak.service.serviceImpl;

import com.config.authenticationwithkeycloak.exception.NotFoundException;
import com.config.authenticationwithkeycloak.model.dto.request.UserRequest;
import com.config.authenticationwithkeycloak.model.entity.User;
import com.config.authenticationwithkeycloak.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIml implements UserService {
    private final Keycloak keycloak;
    private final ModelMapper modelMapper;

    @Value("${keycloak.realm}")
    private String realm;


    public UserServiceIml(Keycloak keycloak, ModelMapper modelMapper) {
        this.keycloak = keycloak;
        this.modelMapper = modelMapper;
    }

    @Override
    public User getCurrentUser(String id) {
        UserRepresentation userRepresentation = keycloak.realm(realm).users().get(id).toRepresentation();
        return modelMapper.map(userRepresentation, User.class);
    }
    private User getUser (UserRepresentation userRepresentation){
        User user = modelMapper.map(userRepresentation, User.class);
        user.setGender(userRepresentation.getAttributes().get("gender").getFirst());
        return user;
    }

    @Override
    public void updateUserByCurrenId(String userId, UserRequest userRequest) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();

            userRepresentation.setEmail(userRequest.getEmail());
            userRepresentation.setFirstName(userRequest.getFirstName());
            userRepresentation.setLastName(userRequest.getLastName());

            userResource.update(userRepresentation);

            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                resetPassword(userResource, userRequest.getPassword());
            }
        } catch (NotFoundException e) {
            throw new RuntimeException("User not found with ID: " + userId, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user with ID: " + userId, e);
        }
    }
    private void resetPassword(UserResource userResource, String newPassword) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        userResource.resetPassword(credential);
    }
}
