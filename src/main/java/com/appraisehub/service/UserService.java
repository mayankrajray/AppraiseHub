package com.appraisehub.service;

import com.appraisehub.dto.UserRequestDTO;
import com.appraisehub.dto.UserResponseDTO;
import com.appraisehub.enums.Role;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO requestDTO);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    List<UserResponseDTO> getAllUsers();
    List<UserResponseDTO> getUsersByDepartment(Long departmentId);
    List<UserResponseDTO> getUsersByRole(Role role);
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);
    UserResponseDTO deactivateUser(Long id);
    UserResponseDTO activateUser(Long id);
}
