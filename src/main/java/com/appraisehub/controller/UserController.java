package com.appraisehub.controller;

import com.appraisehub.dto.ApiResponse;
import com.appraisehub.dto.UserRequestDTO;
import com.appraisehub.dto.UserResponseDTO;
import com.appraisehub.enums.Role;
import com.appraisehub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO savedUser = userService.createUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", savedUser));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(
            @PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByDepartment(
            @PathVariable Long departmentId) {
        List<UserResponseDTO> users = userService.getUsersByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByRole(
            @PathVariable Role role) {
        List<UserResponseDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDTO>> deactivateUser(
            @PathVariable Long id) {
        UserResponseDTO user = userService.deactivateUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("User deactivated successfully", user));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponseDTO>> activateUser(
            @PathVariable Long id) {
        UserResponseDTO user = userService.activateUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("User activated successfully", user));
    }
}