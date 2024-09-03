package com.config.authenticationwithkeycloak.controller;

import com.config.authenticationwithkeycloak.model.dto.request.UserRequest;
import com.config.authenticationwithkeycloak.model.entity.User;
import com.config.authenticationwithkeycloak.service.UserService;
import com.config.authenticationwithkeycloak.utils.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/vi/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "spring_oauth2")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get current User")
    public ResponseEntity<?> getCurrentUser (@AuthenticationPrincipal Jwt jwt){
        User user = userService.getCurrentUser(jwt.getSubject());

        APIResponse<User> apiResponse = APIResponse.<User>builder()
                .message("Get current user successfully!")
                .status(HttpStatus.OK)
                .payload(user)
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserByCurrenId(@PathVariable String userId, @RequestBody UserRequest userRequest) {
        userService.updateUserByCurrenId(userId, userRequest);
        return ResponseEntity.ok("User updated successfully!");
    }
}
