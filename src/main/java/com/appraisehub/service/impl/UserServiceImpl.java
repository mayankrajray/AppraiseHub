package com.appraisehub.service.impl;

import com.appraisehub.dto.UserRequestDTO;
import com.appraisehub.dto.UserResponseDTO;
import com.appraisehub.entity.Department;
import com.appraisehub.entity.User;
import com.appraisehub.enums.Role;
import com.appraisehub.exception.DuplicateResourceException;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.mappers.UserMapper;
import com.appraisehub.repository.DepartmentRepository;
import com.appraisehub.repository.UserRepository;
import com.appraisehub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists: " + requestDTO.getEmail());
        }

        User.UserBuilder builder = User.builder()
                .fullName(requestDTO.getFullName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(requestDTO.getRole())
                .jobTitle(requestDTO.getJobTitle());

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department", requestDTO.getDepartmentId()));
            builder.department(department);
        }

        if (requestDTO.getManagerId() != null) {
            User manager = userRepository
                    .findById(requestDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager", requestDTO.getManagerId()));
            builder.manager(manager);
        }

        User savedUser = userRepository.save(builder.build());
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUsersByDepartment(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId)
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        existing.setFullName(requestDTO.getFullName());
        existing.setEmail(requestDTO.getEmail());
        existing.setRole(requestDTO.getRole());
        existing.setJobTitle(requestDTO.getJobTitle());

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department", requestDTO.getDepartmentId()));
            existing.setDepartment(department);
        }

        if (requestDTO.getManagerId() != null) {
            User manager = userRepository
                    .findById(requestDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager", requestDTO.getManagerId()));
            existing.setManager(manager);
        }

        User updatedUser = userRepository.save(existing);
        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponseDTO deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setActive(false);
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponseDTO activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setActive(true);
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponse(updatedUser);
    }
}