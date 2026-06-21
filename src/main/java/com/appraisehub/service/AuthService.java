package com.appraisehub.service;

import com.appraisehub.dto.AuthResponseDTO;
import com.appraisehub.dto.LoginRequestDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO request);
    AuthResponseDTO getCurrentUser(String email);
}